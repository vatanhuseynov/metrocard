package com.ibar.metrocard.user.service.impl;

import com.ibar.metrocard.auth.dto.request.AuthRequest;
import com.ibar.metrocard.auth.dto.response.AuthResponse;
import com.ibar.metrocard.common.SuccessMessage;
import com.ibar.metrocard.exception.StatusMessage;
import com.ibar.metrocard.user.dto.request.UserRequest;
import com.ibar.metrocard.user.dto.response.UserDetailResponse;
import com.ibar.metrocard.user.mapper.UserMapper;
import com.ibar.metrocard.user.model.Role;
import com.ibar.metrocard.user.model.User;
import com.ibar.metrocard.user.repository.UserRepository;
import com.ibar.metrocard.user.service.RoleService;
import com.ibar.metrocard.user.service.UserService;
import com.ibar.metrocard.utility.IdempotencyKeyService;
import com.ibar.metrocard.utility.JwtGenerateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.ibar.metrocard.exception.StatusMessage.BALANCE_EXCEEDED;
import static com.ibar.metrocard.user.model.RoleEnum.USER;
import static com.ibar.metrocard.utility.AmountConstant.MAX_BALANCE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final RoleService roleService;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerateUtil jwtGenerateUtil;
    private final IdempotencyKeyService idempotencyKeyService;

    @Override
    public User findUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, StatusMessage.NOT_FOUND.getMessage()));
    }

    @Override
    public User findByMail(String mail) {
        return repository.findUserByMail(mail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public User findUserByToken(String token) {
        var userId = jwtGenerateUtil.decodeToken(token).getUserId();
        return findUserById(userId);
    }

    @Override
    @Transactional
    public UserDetails loadByUsername(String username) {
        var user = findByMail(username);
        var authorities = user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }

    @Override
    @Transactional
    public UserDetailResponse findUserDetailByToken(String token) {
        var user = findUserByToken(token);
        return mapper.toDetailResponse(user);
    }

    @Override
    @Transactional
    public AuthResponse login(String mail) {
        var user = findByMail(mail);
        String accessToken = jwtGenerateUtil.createAccessToken(user.getId(), user.getMail(), user.getRoles());
        String refreshToken = jwtGenerateUtil.createRefreshToken(user.getId(), user.getMail());
        return AuthResponse.builder()
                .type("Bearer ")
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse register(UserRequest request) {
        var existingUser = repository.findUserByMail(request.getMail());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(BAD_REQUEST, StatusMessage.ALREADY_EXISTS.getMessage());
        }
        User user = mapper.toEntity(request);
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encryptedPassword);
        user.setBalance(BigDecimal.ZERO);
        //warn: teleblerde yalniz istifadeci olduug ucun default normal user teyin edirem
        Role role = roleService.findByKeyword(USER.getKeyword());
        user.setRoles(List.of(role));
        repository.save(user);
        AuthRequest authRequest = AuthRequest.builder()
                .mail(request.getMail())
                .password(request.getPassword())
                .build();
        return login(authRequest.getMail());
    }

    @Override
    @Transactional
    public SuccessMessage updateBalance(String token, String idempotencyKey, BigDecimal balance) {
        if (idempotencyKeyService.isKeyUsed(idempotencyKey)) {
            return (SuccessMessage) idempotencyKeyService.getResponse(idempotencyKey);
        }
        var user = findUserByToken(token);
        BigDecimal newBalance = balance.add(user.getBalance());
        if (newBalance.compareTo(MAX_BALANCE) > 0) {
            throw new ResponseStatusException(BAD_REQUEST, BALANCE_EXCEEDED.getMessage());
        }
        user.setBalance(newBalance);
        return (SuccessMessage) idempotencyKeyService.useKey(idempotencyKey, SuccessMessage.builder().message(StatusMessage.OK.getMessage()).build());
    }

    @Override
    @Transactional
    public void decreaseBalance(String token, BigDecimal amount) {
        var user = findUserByToken(token);
        BigDecimal newAmount = amount.subtract(user.getBalance());
        user.setBalance(newAmount);
    }

    @Override
    public boolean checkMailExistence(String mail) {
        return repository.existsByMail(mail);
    }


}
