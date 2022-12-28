package com.nofront.plantinggrassbe.Utils;

import com.auth0.jwk.JwkException;

import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class GoogleTokenUtils extends AbstractTokenUtils{

    @Autowired
    Environment env;

    private final String CLIENT_ID;

    public GoogleTokenUtils(Environment env) {
        this.env = env;
        this.CLIENT_ID = env.getProperty("spring.google.client.id") != null ?
                env.getProperty("spring.google.client.id") :
                "";
    }

    @Override
    public boolean validate(String token) throws ParseException, JwkException{
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

        // (Receive idTokenString by HTTPS POST)
        try {
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = (GoogleIdToken.Payload) idToken.getPayload();

                // Print user identifier
                String userId = payload.getSubject();
                System.out.println("User ID: " + userId);

                // Get profile information from payload
                String email = payload.getEmail();

                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                String locale = (String) payload.get("locale");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");

                // Use or store profile information
                // ...

            } else {
                System.out.println("Invalid ID token.");
            }
        }
        catch (Exception e){

        }
        return false;
    }

    @Override
    public OauthInfo getOauthInfo(String token) throws ParseException {
        return OauthInfo.builder()
                .userId(getSubject(token))
                .provider(getIssuer(token))
                .build();
    }

}
