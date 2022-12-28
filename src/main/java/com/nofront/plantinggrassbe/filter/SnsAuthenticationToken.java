package com.nofront.plantinggrassbe.filter;

import com.nofront.plantinggrassbe.DTO.UserDetails;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class SnsAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final String credential;

    public SnsAuthenticationToken(String provider, String token) {
        super(null);
        this.principal = provider;
        this.credential = token;
        this.setAuthenticated(false);
    }

    public SnsAuthenticationToken(UserDetails userDetails, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = userDetails;
        this.credential = null;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credential;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

}
