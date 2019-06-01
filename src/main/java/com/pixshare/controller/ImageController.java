package com.pixshare.controller;

import com.pixshare.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
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

    @PostMapping(value = "/addimage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> addImage(@RequestParam("image") MultipartFile image)  {
        try {
            FileOutputStream fos = new FileOutputStream("/home/user/Desktop/images/" + image.getOriginalFilename());
            fos.write(image.getBytes());
            fos.close();
        }
        catch(IOException e)
        {
        log.error("error",e);
        }
        Response response = new Response(200,"Image Uploaded");
        return new ResponseEntity<Response>(response,HttpStatus.OK);
    }

    @GetMapping(value = "/userimages",produces = MediaType.ALL_VALUE)
    public ResponseEntity<List<ServerProperties.Tomcat.Resource>> getImages(@RequestParam Long id){
    	
    	
    return null;

    }
}
