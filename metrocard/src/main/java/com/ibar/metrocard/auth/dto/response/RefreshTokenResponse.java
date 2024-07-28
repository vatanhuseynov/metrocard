package com.ibar.metrocard.auth.dto.response;

import lombok.*;

import java.io.Serial;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponse {
    @Serial
    private static final long serialVersionUID = 4389187657029610027L;
    private String accessToken;
    private String refreshToken;
    @Builder.Default
    private String tokenType = "Bearer";

}
