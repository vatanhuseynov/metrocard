package com.ibar.metrocard.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {
    ADMIN("admin"),
    USER("user");
    private final String keyword;
}

