package com.project.project.controllers;


import com.project.project.JWT.AuthService;
import com.project.project.requests.jwt_requests.JwtAuthResponse;
import com.project.project.requests.auth_requests.LoginRequest;
import com.project.project.requests.auth_requests.RegisterRequest;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthRestController {

    @Autowired
    private AuthService authenticationService;

    /**
     * The SignUp(RegisterRequest) registers a new user.
     * @param request request for registration.
     * @return an object with a JWT-token.
     */
    @PostMapping("/register")
    public JwtAuthResponse SignUp(@RequestBody @Valid RegisterRequest request)
                                  throws AuthException {
        return authenticationService.Register(request);
    }

    /**
     * The SignIn(RegisterRequest) login an existing user.
     * @param request request for authorization.
     * @return an object with a JWT-token.
     */
    @PostMapping("/login")
    public JwtAuthResponse SignIn(@RequestBody @Valid LoginRequest request) {
        return authenticationService.Authorization(request);
    }
}

