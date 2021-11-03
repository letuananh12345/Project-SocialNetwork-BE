package com.meta.socialnetwork.dto.request;

import lombok.Data;

@Data
public class ChangeProfileForm {
    private String fullName;
    private String email;
    private String phone;
}
