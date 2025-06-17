package de.fhdo.spring.user.context.dto;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    public String oldPassword;
    public String newPassword;
}

