package com.ibar.metrocard.card.repository;

import com.ibar.metrocard.card.model.Card;
import com.ibar.metrocard.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByCardNumber(String cardNumber);

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Card> findById(Long id);

    Page<Card> findAllByUser(User user, Pageable pageable);
}
