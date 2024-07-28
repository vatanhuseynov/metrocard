package com.ibar.metrocard.user.mapper;

import com.ibar.metrocard.user.dto.request.UserRequest;
import com.ibar.metrocard.user.dto.response.UserDetailResponse;
import com.ibar.metrocard.user.dto.response.UserResponse;
import com.ibar.metrocard.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;

@Mapper(componentModel = "spring", nullValueCheckStrategy = ALWAYS, uses = RoleMapper.class)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User toEntity(UserRequest request);

    UserResponse toResponse(User user);

    UserDetailResponse toDetailResponse(User user);
}
