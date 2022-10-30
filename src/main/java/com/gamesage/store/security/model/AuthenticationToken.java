package com.gamesage.store.security.model;

import com.gamesage.store.domain.model.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class AuthenticationToken extends AbstractAuthenticationToken {

    private final User user;
    private final String xAuthToken;


    public AuthenticationToken(String xAuthToken) {
        this(null, xAuthToken, Collections.emptyList());
    }

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public AuthenticationToken(User user, String xAuthToken, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.xAuthToken = xAuthToken;
        this.user = user;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return getUser();
    }

    public User getUser() {
        return user;
    }

    public String getXAuthToken() {
        return xAuthToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AuthenticationToken that = (AuthenticationToken) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user);
    }
}
