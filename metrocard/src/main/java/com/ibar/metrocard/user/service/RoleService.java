package com.ibar.metrocard.user.service;

import com.ibar.metrocard.user.model.Role;

public interface RoleService {
    Role findByKeyword(String keyword);
}
