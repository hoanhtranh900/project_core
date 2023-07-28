package com.osp.core.security;

import com.osp.core.entity.AdmUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collection;


/**
 * TODO: write you class description here
 *
 * @author
 */

@Getter
@Setter
public class TokenBasedAuthentication extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 1L;
    private Object credentials;
    private final AdmUser principal;
    private boolean authenticated = false;

    public TokenBasedAuthentication(AdmUser principal ) {
        super(principal.getAuthorities());
        this.principal = principal;
    }

    public TokenBasedAuthentication(AdmUser principal, Object credentials) {
        super(principal.getAuthorities());
        this.principal = principal;
        this.credentials = credentials;
        this.authenticated = false;
    }

    public TokenBasedAuthentication(AdmUser principal, boolean authenticated) {
        super( principal.getAuthorities() );
        this.principal = principal;
        this.authenticated = authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	@Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public AdmUser getPrincipal() {
        return this.principal;
    }

}
