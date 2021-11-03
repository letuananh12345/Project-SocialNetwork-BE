package com.meta.socialnetwork.dto.request;

import lombok.Data;

@Data
public class ChangeProfileForm {
    private String name;
    private String username;
    private String email;
}
