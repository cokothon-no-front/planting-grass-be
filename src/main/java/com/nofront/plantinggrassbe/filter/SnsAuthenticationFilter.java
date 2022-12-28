package com.nofront.plantinggrassbe.filter;

import com.nofront.plantinggrassbe.DTO.UserDetails;
import com.nofront.plantinggrassbe.Utils.JwtTokenUtils;
import com.nofront.plantinggrassbe.repository.UserRepository;
import com.nofront.plantinggrassbe.service.UserService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;


public class SnsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    final String AUTH_HEADER_NAME = "Authorization";
    final String BEARER_NAME = "Bearer";

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    public SnsAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/oauth/*","POST"), authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("Enter Sns filter");

        String provider = request.getServletPath().substring(7);

        String authHeader = request.getHeader(AUTH_HEADER_NAME);
        if (authHeader == null || authHeader.isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Auth header does not exists");
        }
        String[] authHeaderValue = authHeader.split(" ");
        if (authHeaderValue.length != 2){
            throw new AuthenticationCredentialsNotFoundException("invalid auth header, require token");
        }
        if(!BEARER_NAME.equals(authHeaderValue[0])) {
            throw new BadCredentialsException("invalid auth header, require bearer");
        }
        String jwtToken = authHeaderValue[1];

        return this.getAuthenticationManager().authenticate(new SnsAuthenticationToken(provider, jwtToken));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        try(PrintWriter writer = response.getWriter()) {
            UserDetails userDetails = (UserDetails) authResult.getPrincipal();

            // 토큰 만들어주기
            JwtTokenUtils jwtTokenUtils = new JwtTokenUtils();
            String accessToken = jwtTokenUtils.generateAccessToken(userDetails.getUsername(), userDetails.getProvider());
            String refreshToken = jwtTokenUtils.generateRefreshToken();

            // 디비에 토큰 + 유저 정보 저장
            userService.saveToken(userDetails.getUsername(), userDetails.getProvider(), refreshToken, "tmp");


            JSONObject responseJson = new JSONObject();
            responseJson.put("accessToken", accessToken);
            responseJson.put("refreshToken", refreshToken);

            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

            writer.write(responseJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("filter fail = " + request);
        PrintWriter writer = response.getWriter();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
