package com.ibar.metrocard.card.service.impl;

import com.ibar.metrocard.card.dto.request.CardRequest;
import com.ibar.metrocard.card.dto.response.CardResponse;
import com.ibar.metrocard.card.mapper.CardMapper;
import com.ibar.metrocard.card.model.Card;
import com.ibar.metrocard.card.repository.CardRepository;
import com.ibar.metrocard.card.service.CardService;
import com.ibar.metrocard.common.SuccessMessage;
import com.ibar.metrocard.exception.StatusMessage;
import com.ibar.metrocard.user.model.User;
import com.ibar.metrocard.user.service.UserService;
import com.ibar.metrocard.utility.IdempotencyKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

import static com.ibar.metrocard.exception.StatusMessage.BALANCE_EXCEEDED;
import static com.ibar.metrocard.exception.StatusMessage.NOT_ENOUGH;
import static com.ibar.metrocard.utility.AmountConstant.MAX_BALANCE;
import static com.ibar.metrocard.utility.AmountConstant.METRO_FEE;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final UserService userService;
    private final IdempotencyKeyService idempotencyKeyService;
    private final CardMapper mapper;
    private final CardRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<CardResponse> findAll(String token, Pageable pageable) {
        var user = userService.findUserByToken(token);
        return repository.findAllByUser(user, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CardResponse findById(String token, Long id) {
        var card = findCardById(token, id);
        return mapper.toResponse(card);
    }

    @Override
    @Transactional
    public SuccessMessage addCard(String token, CardRequest request) {
        User user = userService.findUserByToken(token);
        boolean cardCondition = repository.existsByCardNumber(request.getCardNumber());
        if (cardCondition) {
            throw new ResponseStatusException(BAD_REQUEST, StatusMessage.ALREADY_EXISTS.getMessage());
        }
        Card card = mapper.toEntity(request);
        card.setBalance(BigDecimal.ZERO);
        card.setUser(user);
        repository.save(card);
        return SuccessMessage.builder().message(StatusMessage.CREATED.getMessage()).build();
    }

    @Override
    @Transactional
    public SuccessMessage updateCard(String token, Long id, CardRequest request) {
        boolean cardCondition = repository.existsByCardNumber(request.getCardNumber());
        if (cardCondition) {
            throw new ResponseStatusException(BAD_REQUEST, StatusMessage.ALREADY_EXISTS.getMessage());
        }
        Card card = findCardById(token, id);
        mapper.updateCard(request, card);
        repository.save(card);
        return SuccessMessage.builder().message(StatusMessage.OK.getMessage()).build();
    }

    @Override
    @Transactional
    public SuccessMessage updateCardBalance(String token, String idempotencyKey, Long id, BigDecimal balance) {
        if (idempotencyKeyService.isKeyUsed(idempotencyKey)) {
            return (SuccessMessage) idempotencyKeyService.getResponse(idempotencyKey);
        }
        User user = userService.findUserByToken(token);
        BigDecimal userBalance = user.getBalance();
        if (userBalance.compareTo(balance) < 0) {
            throw new ResponseStatusException(BAD_REQUEST, NOT_ENOUGH.getMessage());
        }
        if (balance.compareTo(MAX_BALANCE) > 0) {
            throw new ResponseStatusException(BAD_REQUEST, BALANCE_EXCEEDED.getMessage());
        }
        var card = findCardById(token, id);
        BigDecimal newBalance = card.getBalance().add(balance);
        card.setBalance(newBalance);
        userService.decreaseBalance(token, newBalance);
        return SuccessMessage.builder().message(StatusMessage.OK.getMessage()).build();
    }

    @Override
    @Transactional
    public SuccessMessage useCard(String token, String idempotencyKey, Long id) {
        var card = findCardById(token, id);
        if (idempotencyKeyService.isKeyUsed(idempotencyKey)) {
            return (SuccessMessage) idempotencyKeyService.getResponse(idempotencyKey);
        }
        BigDecimal newBalance = card.getBalance().subtract(METRO_FEE);
        card.setBalance(newBalance);
        return SuccessMessage.builder().message(StatusMessage.OK.getMessage()).build();
    }

    @Override
    @Transactional
    public SuccessMessage deleteById(String token, Long id) {
        var card = findCardById(token, id);
        repository.deleteById(card.getId());
        return SuccessMessage.builder().message(StatusMessage.OK.getMessage()).build();
    }

    private Card findCardById(String token, Long id) {
        var user = userService.findUserByToken(token);
        Card card = repository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, StatusMessage.NOT_FOUND.getMessage()));
        if (!card.getUser().equals(user)) {
            throw new ResponseStatusException(FORBIDDEN, StatusMessage.FORBIDDEN.getMessage());
        }
        return card;

    }
}
