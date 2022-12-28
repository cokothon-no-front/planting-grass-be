package com.nofront.plantinggrassbe.Utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class AmazonS3Utils {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private AmazonS3Client amazonS3Client;

    public void store(String fullPath, MultipartFile multipartFile) {
        try {
            File file = File.createTempFile("test_", ".tmp");
            multipartFile.transferTo(file);
            amazonS3Client.putObject(new PutObjectRequest(bucket, fullPath, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            file.deleteOnExit();
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
    }


    public byte[] getObject(String storedFileName) throws IOException {
        S3Object o = amazonS3.getObject(new GetObjectRequest(bucket, storedFileName));
        S3ObjectInputStream objectInputStream = o.getObjectContent();
        return IOUtils.toByteArray(objectInputStream);

    }
}



