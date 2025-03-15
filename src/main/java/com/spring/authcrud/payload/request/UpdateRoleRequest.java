package com.spring.authcrud.payload.request;

import java.util.Set;

public class UpdateRoleRequest {
    private Set<String> roles;

    public UpdateRoleRequest(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
