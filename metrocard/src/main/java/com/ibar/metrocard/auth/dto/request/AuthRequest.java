package com.ibar.metrocard.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @Email(message = "Yanlis format")
    @NotBlank(message = "Mail  boş ola bilməz!")
    private String mail;
    @NotBlank(message = "Şifrə boş ola bilməz!")
    private String password;
}
