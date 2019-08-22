package com.pixshare.controller;

import com.pixshare.dao.ConnectionsRequest;
import com.pixshare.dao.ImageRepository;

import com.pixshare.dao.PersonConnections;
import com.pixshare.dao.UserRepository;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/api")
@Slf4j
@CrossOrigin(origins="*")
public class ImageController {

	@Autowired
	ImageRepository imageRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ConnectionsRequest connectionRequest;

	@Autowired
	PersonConnections personConnection;
	
	
	@Autowired
	ModelMapper modelMapper;

	private final Path rootLocation = Paths.get("/home/user/Desktop/images");

	@PostMapping(value = "/addimage")
	public ResponseEntity<Response> addImage(@RequestParam("image") MultipartFile image, @RequestParam Long userId) {
		try {
			FileOutputStream fos = new FileOutputStream("/home/user/Desktop/images/" + image.getOriginalFilename());
			UserDetails userDetails = userRepository.findById(userId).get();
			ImageDetails imageDetails = new ImageDetails();
			imageDetails.setAuthor(userDetails.getEmail());
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
	public ResponseEntity<List<String>> getImagesMetaData(@RequestParam Long id) {
		List<Connections> listOfConnectedUser = personConnection.findAll();
		List<Long> idOfUsersList = listOfConnectedUser.stream().map(i -> i.getConnectedUser())
				.collect(Collectors.toList());
		List<String> listofEmails = userRepository.findAllById(idOfUsersList).stream().map(i -> i.getEmail())
				.collect(Collectors.toList());
		List<String> listofImageName = imageRepository.findAllByAuthor(listofEmails).stream().map(i -> i.getImageName())
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

	@PostMapping("/responseconnection")
	public ResponseEntity<Response> responseConnection(@RequestParam Long userId, @RequestParam Long whoSendRequest,
			@RequestParam Boolean response) {
		System.out.println(response.booleanValue() + " " + userId);
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
		List<Long> followUser=personConnection.findAllBySourceUser(userId).stream().map(value ->returnOnlyIds(value)).collect(Collectors.toList());
		List<Long> addUser=connectionRequest.findAllByFromId(userId).stream().map(value -> OnlyIdsfromPendingConnection(value)).collect(Collectors.toList());
		followUser.add(userId);
		followUser.addAll(addUser);
		System.out.println(followUser);
		return userRepository.findByIdNotIn(followUser, page).stream().map(value -> modelMapper.map(value, UserInfo.class)).collect(Collectors.toList());
	}
	
	@GetMapping("/followers")
		public List<UserInfo> followers(@RequestParam Long userId,Pageable page){
			
			List<Connections> followers = personConnection.findAllBySourceUser(userId);
			List<Long> ll = followers.stream().map(value -> value.getConnectedUser()).collect(Collectors.toList());
			List<UserDetails> follower = userRepository.findAllById(ll);
			List<UserInfo> ss=new ArrayList<>();
			followers.stream().forEach( value ->
			
			ss.add(modelMapper.map(value, UserInfo.class));
			
	}
	
	private Long returnOnlyIds(Connections connection) {
		
		return connection.getConnectedUser();
	}
	
	private Long OnlyIdsfromPendingConnection(PendingConnectionRequest penRequest) {
		return penRequest.getToId(); 
	}
	

}
