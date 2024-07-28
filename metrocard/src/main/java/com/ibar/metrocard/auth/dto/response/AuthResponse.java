package com.ibar.metrocard.auth.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String type;
    private String accessToken;
    private String refreshToken;
    private Long userId;
}
