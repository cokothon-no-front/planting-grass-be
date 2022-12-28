package com.nofront.plantinggrassbe.controller;

import com.nofront.plantinggrassbe.DTO.FileDetails;
import com.nofront.plantinggrassbe.domain.StaticURI;
import com.nofront.plantinggrassbe.service.S3Service;
import com.nofront.plantinggrassbe.service.StaticURIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;

@RestController
//@RequestMapping(value = "/upload", produces = APPLICATION_JSON_VALUE)

public class StaticController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private StaticURIService staticURIService;

    @PostMapping("/statics")
    public ResponseEntity<FileDetails> upload(
            @RequestPart("file")MultipartFile multipartFile,
            @RequestPart("name")String name
            ){
        return ResponseEntity.ok(s3Service.save(name, multipartFile));
    }

    @GetMapping("/statics")
    public ResponseEntity<byte[]> download(@RequestParam(value = "name")String name) throws IOException {
        StaticURI uri = staticURIService.findByName(name);
        byte[] loadData = s3Service.load(uri.getUri());

        String fileName = URLEncoder.encode(uri.getName(), "UTF-8").replaceAll("\\+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(loadData.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(loadData, httpHeaders, HttpStatus.OK);
    }

}
