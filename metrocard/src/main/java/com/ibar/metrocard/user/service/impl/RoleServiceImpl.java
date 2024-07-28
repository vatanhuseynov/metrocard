package com.ibar.metrocard.user.service.impl;

import com.ibar.metrocard.user.model.Role;
import com.ibar.metrocard.user.repository.RoleRepository;
import com.ibar.metrocard.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;

    @Override
    public Role findByKeyword(String keyword) {
        return repository.findByKeyword(keyword);
    }
}
