package com.nofront.plantinggrassbe.controller;

import com.nofront.plantinggrassbe.DTO.AiCountRequestDto;
import com.nofront.plantinggrassbe.service.AiService;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AiController {
    @Autowired
    private AiService aiService;

    @GetMapping("/ai/connection")
    public HashMap<String, Object> callAPI() {

        // 0. 결과값을 담을 객체를 생성합니다.
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        try {
            // 1. 타임아웃 설정시 HttpComponentsClientHttpRequestFactory 객체를 생성합니다.
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000); // 타임아웃 설정 5초
            factory.setReadTimeout(5000); // 타임아웃 설정 5초

            //Apache HttpComponents : 각 호스트(IP와 Port의 조합)당 커넥션 풀에 생성가능한 커넥션 수
            HttpClient httpClient = HttpClientBuilder.create()
                    .setMaxConnTotal(50)//최대 커넥션 수
                    .setMaxConnPerRoute(20).build();
            factory.setHttpClient(httpClient);

            // 2. RestTemplate 객체를 생성합니다.
            RestTemplate restTemplate = new RestTemplate(factory);
//끝?
            // 3. header 설정을 위해 HttpHeader 클래스를 생성한 후 HttpEntity 객체에 넣어줍니다.
            HttpHeaders header = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>(header);

            // 4. 요청 URL을 정의해줍니다.
            String url = "http://localhost:8000/";
            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).queryParam("파라미터명", "값").build(false);

            // 5. exchange() 메소드로 api를 호출합니다.
            ResponseEntity<Map> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);

            // 6. 요청한 결과를 HashMap에 추가합니다.
            // HTTP Status Code
            resultMap.put("statusCode", response.getStatusCodeValue());
            // 헤더 정보
            resultMap.put("header", response.getHeaders());
            // 반환받은 실제 데이터 정보
            resultMap.put("body", response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }// end catch
        return resultMap;
    }
    //callAPI
    @PostMapping("/ai/count")
    public String getCount(
            @RequestBody AiCountRequestDto requestBody
    ) throws IOException {
        String fileName = aiService.isExistOrCreateFile(requestBody);

        // 0. 결과값을 담을 객체를 생성합니다.
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        try {
            // 1. 타임아웃 설정시 HttpComponentsClientHttpRequestFactory 객체를 생성합니다.
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000); // 타임아웃 설정 5초
            factory.setReadTimeout(5000); // 타임아웃 설정 5초

            //Apache HttpComponents : 각 호스트(IP와 Port의 조합)당 커넥션 풀에 생성가능한 커넥션 수
            HttpClient httpClient = HttpClientBuilder.create()
                    .setMaxConnTotal(50)//최대 커넥션 수
                    .setMaxConnPerRoute(20).build();
            factory.setHttpClient(httpClient);

            // 2. RestTemplate 객체를 생성합니다.
            RestTemplate restTemplate = new RestTemplate(factory);
//끝?
            // 3. header 설정을 위해 HttpHeader 클래스를 생성한 후 HttpEntity 객체에 넣어줍니다.
            HttpHeaders header = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>(header);

            // 4. 요청 URL을 정의해줍니다.
            String url = "http://localhost:8000/workout/" + fileName;
            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).queryParam("파라미터명", "값").build(false);

            // 5. exchange() 메소드로 api를 호출합니다.
            ResponseEntity<Map> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);

            // 6. 요청한 결과를 HashMap에 추가합니다.
            // HTTP Status Code
            resultMap.put("statusCode", response.getStatusCodeValue());
            // 헤더 정보
            resultMap.put("header", response.getHeaders());
            // 반환받은 실제 데이터 정보
            resultMap.put("body", response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }// end catch
        return resultMap.get("body").toString();
    }

}

