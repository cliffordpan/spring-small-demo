package me.hchome.demouser.security;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;


public class JWTAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;
    public JWTAuthenticationToken(@NonNull String token) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.token = token;
    }

    @Override
    public String getCredentials() {
        return token;
    }

    @Override
    public String getPrincipal() {
        return "jwtToken";
    }
}
