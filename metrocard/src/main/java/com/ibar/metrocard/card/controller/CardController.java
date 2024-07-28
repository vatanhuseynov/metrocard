package com.ibar.metrocard.card.controller;

import com.ibar.metrocard.card.dto.request.CardRequest;
import com.ibar.metrocard.card.dto.response.CardResponse;
import com.ibar.metrocard.card.service.CardService;
import com.ibar.metrocard.common.SuccessMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("card")
public class CardController {
    private final CardService service;

    @GetMapping
    public ResponseEntity<Page<CardResponse>> findCards(@RequestHeader(AUTHORIZATION) String token,
                                                        Pageable pageable) {
        return ResponseEntity.ok(service.findAll(token, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> findCardDetail(@RequestHeader(AUTHORIZATION) String token,
                                                       @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(token, id));
    }

    @PostMapping
    public ResponseEntity<SuccessMessage> saveCard(@RequestHeader(AUTHORIZATION) String token,
                                                   @RequestBody CardRequest request) {
        return new ResponseEntity<>(service.addCard(token, request), CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessMessage> updateBank(@RequestHeader(AUTHORIZATION) String token,
                                                     @PathVariable Long id,
                                                     @Valid @RequestBody CardRequest request) {
        return ResponseEntity.ok(service.updateCard(token, id, request));
    }

    @PutMapping("/update-balance/{id}")
    public ResponseEntity<SuccessMessage> updateBalance(@RequestHeader(AUTHORIZATION) String token,
                                                        @RequestHeader(value = "Idempotency-Key") String idempotencyKey,
                                                        @PathVariable Long id,
                                                        @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(service.updateCardBalance(token, idempotencyKey, id, amount));
    }

    @PatchMapping("/pay/{id}")
    public ResponseEntity<SuccessMessage> useCard(@RequestHeader(AUTHORIZATION) String token,
                                                  @RequestHeader(value = "Idempotency-Key") String idempotencyKey,
                                                  @PathVariable Long id) {
        return ResponseEntity.ok(service.useCard(token, idempotencyKey, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessMessage> deleteCard(@RequestHeader(AUTHORIZATION) String token,
                                                     @PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(token, id));
    }


}
