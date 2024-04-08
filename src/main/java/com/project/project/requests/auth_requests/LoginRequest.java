package com.project.project.requests.auth_requests;


import lombok.Data;


@Data
public class LoginRequest {
    private String username;
    private String password;
}
