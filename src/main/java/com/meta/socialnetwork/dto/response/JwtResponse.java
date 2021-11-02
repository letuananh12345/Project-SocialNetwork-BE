package com.meta.socialnetwork.dto.response;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class JwtResponse {
    String token;
    private Long id;
    private String type = "Bearer";
    private String fullName;
    private String avatarUrl;
    private Collection<? extends GrantedAuthority> roles;


    public JwtResponse() {
    }

    public JwtResponse(String token, Long id, String avatarUrl, String fullName, Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.id = id;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.roles = authorities;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return roles;
    }

    public void setRoles(Collection<? extends GrantedAuthority> roles) {

        this.roles = roles;
    }
}