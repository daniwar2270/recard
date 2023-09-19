package recard.cards.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import recard.cards.model.entity.Card;
import recard.cards.model.payload.response.CardResponse;

public interface CardService {

    CardResponse getById(Long id);

    Page<CardResponse> getCards(Specification<Card> cardSpecification, Pageable pageable);

    Page<CardResponse> getCardsByUserId(
            Long userId,
            Specification<Card> cardSpecification,
            Pageable pageable,
            String availability
    );

    Page<CardResponse> getCardsByDeckId(
            Long deckId,
            Long userId,
            Specification<Card> cardSpecification,
            Pageable pageable
    );
}