package com.ibar.metrocard.card.repository;

import com.ibar.metrocard.card.model.Card;
import com.ibar.metrocard.user.model.User;
import com.ibar.metrocard.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-dev.yml")
public class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Testing that whether save is working")
    public void test_Save(){
        //Given
        User user = new User();
        userRepository.save(user);

        Card card = new Card();
        card.setCardNumber("1234567890123456");
        card.setName("Test Card");
        card.setBalance(BigDecimal.valueOf(100.00));
        card.setUser(user);

        //When
        cardRepository.save(card);

        //Then
        Optional<Card> optionalCard = cardRepository.findById(card.getId());
        assertTrue(optionalCard.isPresent());
        Card savedCard = optionalCard.get();

        assertAll("Verify saved card properties",
                () -> assertEquals(card.getCardNumber(), savedCard.getCardNumber(), "Card number should match"),
                () -> assertEquals(card.getName(), savedCard.getName(), "Card name should match"),
                () -> assertEquals(card.getBalance(), savedCard.getBalance(), "Card balance should match"),
                () -> assertEquals(card.getUser().getId(), savedCard.getUser().getId(), "Associated user ID should match")
        );

    }



}
