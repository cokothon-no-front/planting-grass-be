package com.nofront.plantinggrassbe.filter;

import com.nofront.plantinggrassbe.DTO.UserDetails;
import com.nofront.plantinggrassbe.Utils.JwtTokenUtils;
import com.nofront.plantinggrassbe.Utils.OauthInfo;
import com.nofront.plantinggrassbe.service.UserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;


@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    JwtTokenUtils jwtTokenUtils;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken beforeToken = (JwtAuthenticationToken) authentication;
        String jwtToken = beforeToken.getCredential();

        try {
            if (!jwtTokenUtils.validate(jwtToken)){
                throw  new BadCredentialsException("token not valid");
            }
            OauthInfo oauthInfo = jwtTokenUtils.getOauthInfo(jwtToken);
            String userId = oauthInfo.getUserId();
            String provider = oauthInfo.getProvider();
            UserDetails user = userDetailsService.findUser(userId, provider);
            return new JwtAuthenticationToken(user, user.getAuthorities());
        } catch (ExpiredJwtException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getLocalizedMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}



