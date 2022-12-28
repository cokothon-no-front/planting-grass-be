package com.nofront.plantinggrassbe.service;

import com.nofront.plantinggrassbe.DTO.AiCountRequestDto;
import com.nofront.plantinggrassbe.DTO.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Transactional
@Service
public class AiService {
    public String isExistOrCreateFile(AiCountRequestDto body) throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        double [][] array = body.getData();
        String fileName = username + body.getStartTime() + body.getWorkOut() + ".csv";
        File file = new File("/home/ubuntu/Server-AI/" + fileName);
//        File file = new File("/Users/jykim/Desktop/" + fileName);
        BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
        String newLine = System.lineSeparator();

        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 4; j ++) {
                bw.write(String.valueOf(array[i][j]));
                bw.write(",");
            }
            bw.write(newLine);
        }

        bw.flush();
        bw.close();
        return fileName;
    }
}
