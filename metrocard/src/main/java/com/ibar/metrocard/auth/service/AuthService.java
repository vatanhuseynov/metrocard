package com.ibar.metrocard.auth.service;

import com.ibar.metrocard.auth.dto.request.AuthRequest;
import com.ibar.metrocard.auth.dto.response.AuthResponse;
import com.ibar.metrocard.auth.dto.response.RefreshTokenResponse;
import com.ibar.metrocard.user.dto.request.UserRequest;

public interface AuthService {
    AuthResponse login(AuthRequest request);
    AuthResponse register(UserRequest request);
    RefreshTokenResponse getNewAccessTokenByRefreshToken(String refreshToken);

}
