package com.meta.socialnetwork.controller;

import com.meta.socialnetwork.model.Image;
import com.meta.socialnetwork.service.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("image")
@CrossOrigin(origins = "*")
public class ImageController {
    @Autowired
    ImageService imageService;

    @PostMapping
    public ResponseEntity<Image> createImage(@RequestBody Image image){
        imageService.save(image);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }
}
