package com.nofront.plantinggrassbe.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    final String AUTH_HEADER_NAME = "Authorization";
    final String BEARER_NAME = "Bearer";
    private final AuthenticationManager authenticationManager;

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        List<String> excludeUrlPatterns = new ArrayList<>() { "/register" };
//
//        return excludeUrlPatterns.stream()
//                .anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
//    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        System.out.println("Enter Jwt filter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

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

        try{
            Authentication authToken = this.authenticationManager.authenticate(new JwtAuthenticationToken(jwtToken));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }catch (ResponseStatusException e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return;
        }


        chain.doFilter(request, response);
    }
}
