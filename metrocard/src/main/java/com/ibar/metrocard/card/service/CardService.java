package com.ibar.metrocard.card.service;

import com.ibar.metrocard.card.dto.request.CardRequest;
import com.ibar.metrocard.card.dto.response.CardResponse;
import com.ibar.metrocard.common.SuccessMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface CardService {
    Page<CardResponse> findAll(String token, Pageable pageable);

    CardResponse findById(String token, Long id);

    SuccessMessage addCard(String token, CardRequest request);

    SuccessMessage updateCard(String token, Long id, CardRequest request);

    SuccessMessage updateCardBalance(String token, String idempotencyKey, Long id, BigDecimal balance);

    SuccessMessage useCard(String token, String idempotencyKey, Long id);

    SuccessMessage deleteById(String token, Long id);

}
