package com.ibar.metrocard.card.service.impl;

import com.ibar.metrocard.card.dto.request.CardRequest;
import com.ibar.metrocard.card.dto.response.CardResponse;
import com.ibar.metrocard.card.mapper.CardMapper;
import com.ibar.metrocard.card.model.Card;
import com.ibar.metrocard.card.repository.CardRepository;
import com.ibar.metrocard.common.SuccessMessage;
import com.ibar.metrocard.exception.StatusMessage;
import com.ibar.metrocard.user.model.User;
import com.ibar.metrocard.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private CardRepository repository;

    @Mock
    private CardMapper mapper;

    @InjectMocks
    private CardServiceImpl cardService;

    private String token;
    private User user;
    private Pageable pageable;
    private Card card;
    private List<Card> cards;
    private Page<Card> cardPage;
    private CardResponse cardResponse;
    private CardRequest request;

    @BeforeEach
    void setUp() {
        token = "Bearer some.jwt.token"; // Test token
        user = new User();
        pageable = PageRequest.of(0, 10);
        card = new Card(2L, "144447899", "my card", BigDecimal.valueOf(10), user);
        request = new CardRequest();
        request.setName("my card");
        request.setCardNumber("144447899");
        cards = Collections.singletonList(new Card());
        cardPage = new PageImpl<>(cards, pageable, cards.size());
        cardResponse = new CardResponse();
        when(userService.findUserByToken(token)).thenReturn(user);
    }


    @Test
    @DisplayName("findAll service test")
    void testFindAll() {
        when(repository.findAllByUser(user, pageable)).thenReturn(cardPage);
        when(mapper.toResponse(any(Card.class))).thenReturn(cardResponse);
        Page<CardResponse> result = cardService.findAll(token, pageable);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userService).findUserByToken(token);
        verify(repository).findAllByUser(user, pageable);
        verify(mapper).toResponse(cards.get(0));
    }

    @Test
    @DisplayName("findbyID with success")
    void testFindById_success() {
        when(repository.findById(anyLong())).thenReturn(java.util.Optional.of(card));
        when(mapper.toResponse(card)).thenReturn(cardResponse);
        CardResponse result = cardService.findById(token, card.getId());
        assertEquals(cardResponse, result);
    }

    @Test
    @DisplayName("findById id with not found")
    void testFindById() {
        when(repository.findById(4L)).thenReturn(java.util.Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cardService.findById(token, 4L);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(StatusMessage.NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("add card with success")
    void test_addCard_success() {
        when(mapper.toEntity(any(CardRequest.class))).thenReturn(card);
        when(repository.existsByCardNumber(request.getCardNumber())).thenReturn(false);
        SuccessMessage result = cardService.addCard(token, request);
        verify(repository, times(1)).save(card);
        assertEquals(StatusMessage.CREATED.getMessage(), result.getMessage());

    }

    @Test
    @DisplayName("add card with exception")
    void testAddCardThrowsBadRequestWhenCardAlreadyExists() {
        when(repository.existsByCardNumber(request.getCardNumber())).thenReturn(true);
        assertThrows(ResponseStatusException.class, () -> {
            cardService.addCard(token, request);
        }, StatusMessage.ALREADY_EXISTS.getMessage());
        verify(repository, times(0)).save(any(Card.class));
    }

}
