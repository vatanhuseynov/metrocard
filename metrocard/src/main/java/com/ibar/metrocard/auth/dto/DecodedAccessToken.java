package com.ibar.metrocard.auth.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DecodedAccessToken {
    private Long userId;
    private String username;
    private List<String> roles;
}
