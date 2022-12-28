package com.nofront.plantinggrassbe.filter;

import com.nofront.plantinggrassbe.DTO.UserDetails;
import com.nofront.plantinggrassbe.Utils.KakaoTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class KakaoAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    KakaoTokenUtils kakaoTokenUtils;
    private final String provider = "kakao";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(!provider.equals(authentication.getPrincipal())) return null;

        String token = (String) authentication.getCredentials();
        try {
            if (!kakaoTokenUtils.validate(token)) return null;
            String id = kakaoTokenUtils.getOauthInfo(token).getUserId();

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
