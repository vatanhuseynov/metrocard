package com.ibar.metrocard.auth.controller;

import com.ibar.metrocard.auth.dto.request.AuthRequest;
import com.ibar.metrocard.auth.dto.response.AuthResponse;
import com.ibar.metrocard.auth.dto.response.RefreshTokenResponse;
import com.ibar.metrocard.auth.service.AuthService;
import com.ibar.metrocard.user.dto.request.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("public")
public class AuthController {
    private final AuthService service;

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping("register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping(value = "/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return ResponseEntity.ok(service.getNewAccessTokenByRefreshToken(refreshToken));
    }

}
