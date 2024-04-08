package com.project.project.JWT;


import com.project.project.requests.jwt_requests.JwtAuthResponse;
import com.project.project.requests.auth_requests.LoginRequest;
import com.project.project.requests.auth_requests.RegisterRequest;
import com.project.project.user_config.User;
import com.project.project.user_config.UserRole;
import com.project.project.user_config.UserServiceManager;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AuthService {

    @Autowired
    private UserServiceManager userServiceManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * The Authorization(LoginRequest) authorizes the user.
     * @param request request for authorization.
     * @return response, that contains jwt token.
     */
    public JwtAuthResponse Authorization(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                ));
        UserDetails user = userServiceManager
                .UserDetailsService()
                .loadUserByUsername(request.getUsername());

        return new JwtAuthResponse(jwtService.GenerateTokenValue(user));
    }

    /**
     * The Register(RegisterRequest) registers the user.
     * @param request request for registration.
     * @return response, that contains jwt token.
     */
    public JwtAuthResponse Register(RegisterRequest request) throws AuthException {

        if(userServiceManager.IsExist(request.getUsername()))
            throw new AuthException("So user already exists.");

        User user = User
                .builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ROLE_USER)
                .build();
        userServiceManager.Add(user);

        return new JwtAuthResponse(jwtService.GenerateTokenValue(user));
    }


}
