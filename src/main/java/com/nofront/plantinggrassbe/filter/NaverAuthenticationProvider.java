package com.nofront.plantinggrassbe.filter;

import com.nofront.plantinggrassbe.DTO.UserDetails;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class NaverAuthenticationProvider implements AuthenticationProvider {

    private final String provider = "naver";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(!provider.equals(authentication.getPrincipal())) return null;

        String token = (String) authentication.getCredentials();// 네아로 접근 토큰 값";
        String header = "Bearer " + token; // Bearer 다음에 공백 추가
        try {
            String apiURL = "https://openapi.naver.com/v1/nid/me";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", header);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                return null;
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response.toString());
            JSONParser parser = new JSONParser();
            JSONObject responseObj = (JSONObject) parser.parse(String.valueOf(response));
            JSONObject userData = (JSONObject) responseObj.get("response");
            String id = (String) userData.get("id");

            UserDetails userDetails = UserDetails.builder()
                    .username(id)
                    .provider(provider)
                    .isLocked(false)
                    .isExpired(false)
                    .isEnabled(true)
                    .build();

            return new SnsAuthenticationToken(userDetails, userDetails.getAuthorities());

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SnsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
