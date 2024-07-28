package com.ibar.metrocard.user.service;


import com.ibar.metrocard.auth.dto.response.AuthResponse;
import com.ibar.metrocard.common.SuccessMessage;
import com.ibar.metrocard.user.dto.request.UserRequest;
import com.ibar.metrocard.user.dto.response.UserDetailResponse;
import com.ibar.metrocard.user.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;

public interface UserService {
    User findUserById(Long id);
    User findByMail(String mail);
    User findUserByToken(String token);
    UserDetails loadByUsername(String username);
    UserDetailResponse findUserDetailByToken(String token);
    AuthResponse login(String  mail);
    AuthResponse register(UserRequest request);
    SuccessMessage updateBalance(String token, String idempotencyKey, BigDecimal balance);
    void decreaseBalance(String token, BigDecimal amount);
    boolean checkMailExistence(String mail);}
