package com.ibar.metrocard.user.controller;

import com.ibar.metrocard.common.SuccessMessage;
import com.ibar.metrocard.user.dto.response.UserDetailResponse;
import com.ibar.metrocard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService service;

    @GetMapping("/detail")
    public ResponseEntity<UserDetailResponse> findDetailByToken(@RequestHeader(AUTHORIZATION) String token) {
        return ResponseEntity.ok(service.findUserDetailByToken(token));
    }

    @PutMapping("/update-balance")
    public ResponseEntity<SuccessMessage> updateBalance(@RequestHeader(AUTHORIZATION) String token,
                                                        @RequestHeader(value = "Idempotency-Key") String idempotencyKey,
                                                        @RequestParam("amount") BigDecimal amount
    ) {
        return ResponseEntity.ok(service.updateBalance(token, idempotencyKey, amount));
    }


}
