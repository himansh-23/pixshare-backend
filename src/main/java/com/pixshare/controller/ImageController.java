package com.pixshare.controller;

import com.pixshare.dao.ConnectionsRequest;
import com.pixshare.dao.ImageRepository;

import com.pixshare.dao.PersonConnections;
import com.pixshare.dao.UserRepository;
import com.pixshare.dto.ImageDescription;
import com.pixshare.dto.UserInfo;
import com.pixshare.entity.Connections;
import com.pixshare.entity.ImageDetails;
import com.pixshare.entity.PendingConnectionRequest;
import com.pixshare.entity.UserDetails;
import com.pixshare.response.Response;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/api")
@Slf4j
@CrossOrigin(origins="*")
public class ImageController {

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ConnectionsRequest connectionRequest;

	@Autowired
	private PersonConnections personConnection;
	
	
	@Autowired
	private ModelMapper modelMapper;

	private final Path rootLocation = Paths.get("/home/user/Desktop/images");

	@PostMapping(value = "/addimage")
	public ResponseEntity<Response> addImage(@RequestParam("image") MultipartFile image, @RequestParam Long userId,@RequestParam String location,@RequestParam String description) {
		try {
			FileOutputStream fos = new FileOutputStream("/home/user/Desktop/images/" + image.getOriginalFilename());
			UserDetails userDetails = userRepository.findById(userId).get();
			ImageDetails imageDetails = new ImageDetails();
			imageDetails.setAuthor(userDetails.getEmail());
			System.out.println(UUID.randomUUID().toString());
			imageDetails.setLocation(location);
			imageDetails.setDescription(description);
			imageDetails.setImageName(image.getOriginalFilename());
			fos.write(image.getBytes());
			fos.close();
			imageRepository.save(imageDetails);
		} catch (IOException e) {
			log.error("error", e);
		}
		Response response = new Response(200, "Image Uploaded");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/userimages")
	public ResponseEntity<List<ImageDescription>> getImagesMetaData(@RequestParam Long id) {
		List<Connections> listOfConnectedUser = personConnection.findAllBySourceUser(id);
		List<Long> idOfUsersList = listOfConnectedUser.stream().map(i -> i.getConnectedUser())
				.collect(Collectors.toList());
		
		List<String> listofEmails = userRepository.findAllById(idOfUsersList).stream().map(i -> i.getEmail())
				.collect(Collectors.toList());
		log.info(listofEmails+"");
		
		List<ImageDescription> listofImageName = imageRepository.findAllByAuthorIn(listofEmails).stream().map(i -> modelMapper.map(i,ImageDescription.class)) //i.getImageName()
				.collect(Collectors.toList());
		return ResponseEntity.ok(listofImageName);
		
	}
	@GetMapping("/getimage/{name:.+}")
	public Resource getSingleImage(@PathVariable String name) throws MalformedURLException{
		Path path=rootLocation.resolve(name);
		Resource resource= new UrlResource(path.toUri());
		return resource;
	}

	@GetMapping("/createconnection")
	public ResponseEntity<Response> createConnection(@RequestParam Long userId, @RequestParam Long connectionId) {

		PendingConnectionRequest newConnectionRequest = new PendingConnectionRequest();
		newConnectionRequest.setFromId(userId);
		newConnectionRequest.setToId(connectionId);
		connectionRequest.save(newConnectionRequest);

		Response response = new Response(200, "Connection Request Send");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@GetMapping("/responseconnection")
	public ResponseEntity<Response> responseConnection(@RequestParam Long userId, @RequestParam Long whoSendRequest,
			@RequestParam Boolean response) {
		log.info(response + " " + userId);
		if (response.booleanValue()) {
			Connections conn = new Connections();
			conn.setSourceUser(whoSendRequest);
			conn.setConnectedUser(userId);

			Long reqid = connectionRequest.findByRequestId(whoSendRequest, userId).get();
			System.out.println(reqid);
			connectionRequest.deleteById(reqid);
			personConnection.save(conn);
		} else {
			Long req = connectionRequest.findByRequestId(whoSendRequest, userId).get();
			connectionRequest.deleteById(req);
		}
		Response responsess = new Response(200, "Done");
		return new ResponseEntity<Response>(responsess, HttpStatus.OK);
	}
	
	@GetMapping("/follwingsuggestion")
	public List<UserInfo> followingSuggestion(@RequestParam Long userId,Pageable page){
		System.out.println(page);
		log.info(String.valueOf(userId));
		List<Long> followUser=personConnection.findAllBySourceUser(userId).stream().map(this::returnOnlyIds).collect(Collectors.toList());
		List<Long> addUser=connectionRequest.findAllByFromId(userId).stream().map(this::OnlyIdsfromPendingConnection).collect(Collectors.toList());
		followUser.add(userId);
		followUser.addAll(addUser);
		System.out.println(followUser);
		return userRepository.findByIdNotIn(followUser, page).stream().map(value -> modelMapper.map(value, UserInfo.class)).collect(Collectors.toList());
	}
	
	@GetMapping("/followers")
		public List<UserInfo> followers(@RequestParam Long userId,Pageable page){

			List<Connections> followers = personConnection.findAllBySourceUser(userId);
			List<Long> ll = followers.stream().map(Connections::getConnectedUser).collect(Collectors.toList());
			List<UserDetails> follower = userRepository.findAllById(ll);
			List<UserInfo> ss=new ArrayList<>();
			follower.forEach( value ->
			ss.add(modelMapper.map(value, UserInfo.class)));
			return ss;
	}
	
	@GetMapping("/following")
	public List<UserInfo> following(@RequestParam Long userId,Pageable page){
		
		List<Connections> following =personConnection.findAllByConnectedUser(userId);
		List<Long> ids=following.stream().map(Connections::getSourceUser).collect(Collectors.toList());
		List<UserDetails> aa=userRepository.findAllById(ids);
		List<UserInfo> ss=new ArrayList<>();
		aa.forEach(value -> ss.add(modelMapper.map(value, UserInfo.class)));
		return ss;				
	}
	
	private Long returnOnlyIds(Connections connection) {
		
		return connection.getConnectedUser();
	}
	
	private Long OnlyIdsfromPendingConnection(PendingConnectionRequest penRequest) {
		return penRequest.getToId(); 
	}
	
	@GetMapping("/personalImage")
	public ResponseEntity<List<ImageDescription>> getPersonalImageMetaData(@RequestParam Long userId ) throws Exception {
		
		String email=userRepository.findById(userId).map(user ->user.getEmail()).orElseThrow(Exception::new);
		List<ImageDescription> list=imageRepository.findAllByAuthor(email).stream().map(image -> modelMapper.map(image,ImageDescription.class)).collect(Collectors.toList());
		return ResponseEntity.ok().body(list);	
	}
	
	@GetMapping("/getFollowRequests")
	public List<UserInfo>  followRequests(@RequestParam Long userId){
		
	List<Long> users=connectionRequest.findAllByToId(userId).stream().map(PendingConnectionRequest::getFromId).collect(Collectors.toList());
	log.info("get followers"+users);
	List<UserInfo> findUsers=userRepository.findAllById(users).stream().map(value -> modelMapper.map(value,UserInfo.class)).collect(Collectors.toList());
	
	return findUsers;	
	}
}
