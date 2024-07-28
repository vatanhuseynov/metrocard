package com.ibar.metrocard.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoleResponse {
    Long id;
    String keyword;
    String name;
}
