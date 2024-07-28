package com.ibar.metrocard.auth.service.impl;

import com.ibar.metrocard.auth.dto.request.AuthRequest;
import com.ibar.metrocard.auth.dto.response.AuthResponse;
import com.ibar.metrocard.auth.dto.response.RefreshTokenResponse;
import com.ibar.metrocard.auth.service.AuthService;
import com.ibar.metrocard.user.dto.request.UserRequest;
import com.ibar.metrocard.user.service.UserService;
import com.ibar.metrocard.utility.JwtGenerateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtGenerateUtil jwtGenerateUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(AuthRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.getMail(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return userService.login(request.getMail());
    }

    @Override
    public AuthResponse register(UserRequest request) {
        return userService.register(request);
    }

    @Override
    public RefreshTokenResponse getNewAccessTokenByRefreshToken(String refreshToken) {
        var decodedRefreshToken = jwtGenerateUtil.decodeRefreshToken(refreshToken);
        var username = decodedRefreshToken.getUsername();
        var userId = decodedRefreshToken.getId();
        var user = userService.findUserById(userId);

        var newRefreshToken = jwtGenerateUtil.createRefreshToken(userId, username);
        var token = jwtGenerateUtil.createAccessToken(userId, username, user.getRoles());
        return RefreshTokenResponse.builder()
                .accessToken(token)
                .refreshToken(newRefreshToken)
                .build();
    }


}
