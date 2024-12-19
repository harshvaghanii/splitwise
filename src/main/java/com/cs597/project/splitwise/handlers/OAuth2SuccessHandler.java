package com.cs597.project.splitwise.handlers;

import com.cs597.project.splitwise.dto.UserDTO;
import com.cs597.project.splitwise.entities.UserEntity;
import com.cs597.project.splitwise.services.JWTService;
import com.cs597.project.splitwise.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final JWTService jwtService;
    @Value("${deploy.env}")
    private String environment;
    @Value("${frontEndUrl}")
    String frontEndUrl;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) token.getPrincipal();

        String userEmail = oAuth2User.getAttribute("email");
        Optional<UserDTO> user = userService.getUserByEmail(userEmail);
        UserEntity savedUser;
        if (user.isEmpty()) {
            UserEntity newUser = UserEntity.builder()
                    .email(userEmail)
                    .name(oAuth2User.getAttribute("name"))
                    .password(passwordEncoder.encode("dummy-oauth2-password"))
                    .build();
            savedUser = userService.save(newUser);
        } else {
            savedUser = modelMapper.map(user.get(), UserEntity.class);
        }

        String accessToken = jwtService.generateAccessToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(environment));
        response.addCookie(cookie);
        getRedirectStrategy().sendRedirect(request, response, frontEndUrl + accessToken);

    }


}
