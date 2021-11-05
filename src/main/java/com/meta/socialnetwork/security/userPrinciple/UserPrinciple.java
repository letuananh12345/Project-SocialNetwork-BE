package com.meta.socialnetwork.security.userPrinciple;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meta.socialnetwork.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserPrinciple implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String phone;
    @JsonIgnore
    private String password;
    private String avatarUrl;
    private Boolean isActive;

    private Collection<? extends GrantedAuthority> roles;

    public UserPrinciple(Long id, String fullName, String username, String email, String phone, String password, String avatarUrl, Boolean isActive
            , Collection<? extends GrantedAuthority> roles
    ) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.avatarUrl = avatarUrl;
        this.isActive = isActive;
        this.roles = roles;
    }

    //Hàm build mục đích là build user ở trong request,lưu vào một vùng nhớ static
    public static UserPrinciple build(User user) {
        //Convert từ set<> sang list<>(set<AppRole> sang List<GrantedAuthority> )
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
        return new UserPrinciple(
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getPassword(),
                user.getAvatarUrl(),
                user.getIsActive(),
                authorities
        );
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
