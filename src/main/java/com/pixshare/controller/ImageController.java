package com.pixshare.controller;

import com.pixshare.dao.ConnectionsRequest;
import com.pixshare.dao.ImageRepository;
import com.pixshare.dao.UserRepository;
import com.pixshare.entity.ImageDetails;
import com.pixshare.entity.PendingConnectionRequest;
import com.pixshare.entity.UserDetails;
import com.pixshare.response.Response;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user/api")
@Slf4j
public class ImageController {

	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ConnectionsRequest connectionRequest;
	
    @PostMapping(value = "/addimage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> addImage(@RequestParam("image") MultipartFile image,@RequestParam Long userId)  {
        try {
            FileOutputStream fos = new FileOutputStream("/home/user/Desktop/images/" + image.getOriginalFilename());
            UserDetails userDetails = userRepository.findById(userId).get();
            ImageDetails imageDetails=new ImageDetails();
            imageDetails.setAuthor(userDetails.getEmail());
            imageDetails.setImageName(image.getOriginalFilename());
            fos.write(image.getBytes());
            fos.close();
            imageRepository.save(imageDetails);
        }
        catch(IOException e)
        {
        log.error("error",e);
        }
        Response response = new Response(200,"Image Uploaded");
        return new ResponseEntity<Response>(response,HttpStatus.OK);
    }

    @GetMapping(value = "/userimages",produces = MediaType.ALL_VALUE)
    public ResponseEntity<List<Resource>> getImages(@RequestParam Long id){
    	
    	return null;
    }
    
    @PostMapping("/createconnection")
    public ResponseEntity<Response> createConnection(@RequestParam Long userId,@RequestParam Long connectionId){
    	
    	PendingConnectionRequest newConnectionRequest=new PendingConnectionRequest();
    	newConnectionRequest.setFromId(userId);
    	newConnectionRequest.setToId(connectionId);
    	connectionRequest.save(newConnectionRequest);
    	
    	 Response response = new Response(200,"Connection Request Send");
         return new ResponseEntity<Response>(response,HttpStatus.OK);
    }
    
    @PostMapping("/responseconnection")
    public ResponseEntity<Response> responseConnection(@RequestParam Long userId,@RequestParam Long whoSendRequest,@RequestParam Boolean response){
    
    return null;
    }
}
