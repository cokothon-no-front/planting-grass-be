package com.nofront.plantinggrassbe.filter;

import com.nofront.plantinggrassbe.DTO.UserDetails;
import com.nofront.plantinggrassbe.Utils.GoogleTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class GoogleAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    GoogleTokenUtils googleTokenUtils;
    private final String provider = "google";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(!provider.equals(authentication.getPrincipal())) return null;

        String token = (String) authentication.getCredentials();
        try {
            if (!googleTokenUtils.validate(token)) return null;
            String id = googleTokenUtils.getOauthInfo(token).getUserId();

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
