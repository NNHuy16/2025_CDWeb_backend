package com.example.jobSeaching.security;

import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.service.UsersService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UsersService usersService;
    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2SuccessHandler(UsersService usersService, JwtTokenProvider jwtTokenProvider) {
        this.usersService = usersService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = authToken.getAuthorizedClientRegistrationId();

        User user;
        if ("google".equals(registrationId)) {
            user = usersService.loginReisterUserGoogle(authToken);
//        } else if ("facebook".equals(registrationId)) {
//            user = usersService.loginReisterUserFacebook(authToken);
        } else {
            throw new IllegalArgumentException("Unsupported OAuth2 provider: " + registrationId);
        }

        Authentication springAuth = new UsernamePasswordAuthenticationToken(user.getEmail(), null,
                List.of(() -> "ROLE_" + user.getRole().name()));
        String jwt = jwtTokenProvider.generateToken(springAuth);

        response.sendRedirect("http://localhost:5173/oauth2/success?token=" + jwt);
    }
}