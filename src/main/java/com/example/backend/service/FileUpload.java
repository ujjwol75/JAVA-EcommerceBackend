package com.example.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUpload {

    public String uploadImage(String path, MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        //if two images of same name exist then this code needs to be done...
        String randomImageName = UUID.randomUUID().toString();
        String randomImageNamewithExtension = randomImageName.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
        // missing path
        String fullpath =path+File.separator + randomImageNamewithExtension;

        File folderFile = new File(path);
        if(!folderFile.exists()){
            folderFile.mkdirs();
        }

        try {
            Files.copy(file.getInputStream(), Paths.get(fullpath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return randomImageNamewithExtension;
    }

}
