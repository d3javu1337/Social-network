package org.d3javu.bd.models.user;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
    admin,
    support,
    user;

    @Override
    public String getAuthority() {
        return name();
    }
}
