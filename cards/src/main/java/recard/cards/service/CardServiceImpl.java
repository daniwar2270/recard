package recard.cards.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import recard.cards.constant.CardConstants;
import recard.cards.constant.DeckConstants;
import recard.cards.constant.OperationConstants;
import recard.cards.exception.CardNotFound;
import recard.cards.mapper.CardMapper;
import recard.cards.model.entity.Card;
import recard.cards.model.payload.response.CardResponse;
import recard.cards.repository.CardRepository;
import recard.cards.specification.CardSpecification;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Override
    public CardResponse getById(Long id) {
        return this.cardRepository.findById(id)
                .map(this.cardMapper::map)
                .orElseThrow(CardNotFound::new);
    }

    @Override
    public Page<CardResponse> getCards(Specification<Card> cardSpecification, Pageable pageable) {
        return this.cardRepository
                .findAll(cardSpecification, pageable)
                .map(this.cardMapper::map);
    }

    @Override
    public Page<CardResponse> getCardsByUserId(
            Long userId,
            Specification<Card> cardSpecification,
            Pageable pageable,
            String availability
    ) {
        Specification<Card> availabilitySpec = null;

        if (CardConstants.AVAILABLE_CARDS.equals(availability)) {
            availabilitySpec = CardSpecification.ofDeckField(
                    DeckConstants.NAME,
                    OperationConstants.EQUAL,
                    DeckConstants.RESERVE_DECK_NAME
            );
        } else if (CardConstants.UNAVAILABLE_CARDS.equals(availability)) {
            availabilitySpec = CardSpecification.ofDeckField(
                    DeckConstants.NAME,
                    OperationConstants.NOT_EQUAL,
                    DeckConstants.RESERVE_DECK_NAME
            );
        }

        return this.cardRepository
                .findAll(
                        CardSpecification.ofDeckField(DeckConstants.PLAYER_ID, OperationConstants.EQUAL, userId)
                                .and(availabilitySpec)
                                .and(cardSpecification),
                        pageable
                )
                .map(this.cardMapper::map);
    }

    @Override
    public Page<CardResponse> getCardsByDeckId(
            Long deckId,
            Long userId,
            Specification<Card> cardSpecification,
            Pageable pageable
    ) {
        return this.cardRepository
                .findAll(
                        CardSpecification.ofDeckField(DeckConstants.ID, OperationConstants.EQUAL, deckId)
                                .and(CardSpecification.ofDeckField(
                                        DeckConstants.PLAYER_ID,
                                        OperationConstants.EQUAL,
                                        userId
                                ))
                                .and(cardSpecification),
                        pageable
                )
                .map(this.cardMapper::map);
    }
}