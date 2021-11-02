package com.meta.socialnetwork.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class SignUpForm {
    private String fullName;
    private String username;
    private String password;
    private String re_password;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private Set<String> roles;
}
