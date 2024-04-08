package com.project.project.requests.jwt_requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class JwtAuthResponse {
    private String JwtToken;
}
