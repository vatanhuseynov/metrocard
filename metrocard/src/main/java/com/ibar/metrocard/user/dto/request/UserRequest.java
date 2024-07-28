package com.ibar.metrocard.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {

    @NotEmpty(message = "Ad boş ola bilməz")
    private String name;

    @NotEmpty(message = "Soyad boş ola bilməz")
    private String surname;

    @NotEmpty(message = "FİN boş ola bilməz")
    private String pin;

    @Email(message ="Yanlış Format")
    @NotEmpty(message = "Email boş ola bilməz")
    private String mail;

    private String password;

    @NotEmpty(message = "FİN boş ola bilməz")
    private String mobileNumber;
}
