package com.ibar.metrocard.user.repository;

import com.ibar.metrocard.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByKeyword(String name);
    boolean existsByKeyword(String keyword);
}
