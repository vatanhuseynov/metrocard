package com.ibar.metrocard.user.mapper;

import com.ibar.metrocard.user.dto.response.RoleResponse;
import com.ibar.metrocard.user.model.Role;
import org.mapstruct.Mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;

@Mapper(componentModel = "spring", nullValueCheckStrategy = ALWAYS)
public interface RoleMapper {
    RoleResponse toResponse(Role role);
}
