package com.nofront.plantinggrassbe.service;

import com.nofront.plantinggrassbe.DTO.FileDetails;
import com.nofront.plantinggrassbe.Utils.AmazonS3Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class S3Service {
    @Autowired
    private AmazonS3Utils amazonS3Utils;
    @Autowired
    private StaticURIService staticURIService;

    public FileDetails save(String name, MultipartFile multipartFile) {
        FileDetails fileDetails = FileDetails.multipartOf(multipartFile);
        amazonS3Utils.store(fileDetails.getPath(), multipartFile);
        staticURIService.save(name, fileDetails.getPath());
        return fileDetails;
    }

    public byte[] load(String uri) throws IOException {
        return amazonS3Utils.getObject(uri);
    }
}
