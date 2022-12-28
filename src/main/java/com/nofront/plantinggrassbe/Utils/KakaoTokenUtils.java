package com.nofront.plantinggrassbe.Utils;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.TimeUnit;

@Component
public class KakaoTokenUtils extends AbstractTokenUtils {

    @Autowired
    Environment env;

    private final JwkProvider kakaoProvider;
    private final String kakaoNativeAppKey;
    private final String PROVIDER_KAKAO = "https://kauth.kakao.com";

    public KakaoTokenUtils(Environment env) {
        this.env = env;
        this.kakaoProvider = new JwkProviderBuilder(PROVIDER_KAKAO)
                .cached(10, 7, TimeUnit.DAYS) // 7일간 최대 10개 캐시
                .build();
        this.kakaoNativeAppKey = env.getProperty("spring.kakao.nativeappkey") != null ?
                env.getProperty("spring.kakao.nativeappkey") :
                "";
    }


    @Override
    public boolean validate(String token) throws ParseException, JwkException {
        String payload = getJsonPayload(token);
        JSONParser parser = new JSONParser();

        //extract iss
        JSONObject payloadObj = (JSONObject) parser.parse(payload);
        String iss = getIssuer(token);

        //check iss
        if (!PROVIDER_KAKAO.equals(iss)) {
            System.out.println("iss = " + iss);
            return false;
        }

        //extract aud
        String aud = (String) payloadObj.get("aud");

        //check aud
        if (!kakaoNativeAppKey.equals(aud)) {
            System.out.println("env.getProperty(\"spring.kakao.nativeappkey\") = " + env.getProperty("spring.kakao.nativeappkey"));
            System.out.println("aud = " + aud);
            return false;
        }
        //extract exp
        long exp = (long) payloadObj.get("exp");

        //check exp
        long curTime = System.currentTimeMillis()/1000;
        if(curTime >= exp){
            System.out.println("curTime = " + curTime);
            System.out.println("exp = " + exp);
            return false;
        }

// 1. 검증없이 디코딩
        DecodedJWT jwtOrigin = JWT.decode(token);
//        System.out.println("jwtOrigin = " + jwtOrigin.getToken());

// 2. 공개키 프로바이더 준비
        Jwk jwk = kakaoProvider.get(jwtOrigin.getKeyId());

// 3. 검증 및 디코딩
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        DecodedJWT decodedJwt = verifier.verify(token);
//        System.out.println("decodedJwt = " + decodedJwt.getToken());
        return true;
    }



    @Override
    public OauthInfo getOauthInfo(String token) throws ParseException {
        return OauthInfo.builder()
                .userId(getSubject(token))
                .provider(PROVIDER_KAKAO)
                .build();
    }

}
