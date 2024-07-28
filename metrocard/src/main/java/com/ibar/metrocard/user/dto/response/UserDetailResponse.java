package com.ibar.metrocard.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserDetailResponse {
    private Long id;
    protected String name;
    protected String surname;
    private String mobileNumber;
    private String pin;
    private BigDecimal balance;
    private List<RoleResponse> roles;

}






