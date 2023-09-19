package recard.cards.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import recard.cards.constant.DeckConstants;
import recard.cards.constant.KafkaConstants;
import recard.cards.constant.OperationConstants;
import recard.cards.constant.TopicConstants;
import recard.cards.exception.CanNotCreateDeck;
import recard.cards.exception.DeckNameForbidden;
import recard.cards.exception.DeckNotFound;
import recard.cards.exception.DeckOverSizeLimitException;
import recard.cards.exception.NotEnoughCardsInDeck;
import recard.cards.model.entity.Card;
import recard.cards.mapper.DeckMapper;
import recard.cards.model.entity.Deck;
import recard.cards.model.enums.Rarity;
import recard.cards.model.payload.request.RemoveCardsRequest;
import recard.cards.model.payload.request.RenameDeckRequest;
import recard.cards.model.payload.response.DeckResponse;
import recard.cards.model.payload.response.events.UserIdEvent;
import recard.cards.repository.CardRepository;
import recard.cards.repository.DeckRepository;
import recard.cards.specification.DeckSpecification;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DeckServiceImpl implements DeckService {

    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;
    private final DeckMapper deckMapper;

    @Override
    @Transactional
    public void moveCardsToDeck(Long userId, Long targetDeckId, List<Long> cardIds) {
        final var reserveDeck = this.findPlayerReserveDeck(userId);
        final var targetDeck = this.findTargetDeckByDeckIdAndPlayer(userId, targetDeckId);

        final var removedCards = this.removeCardsFromDeck(reserveDeck, cardIds);

        this.validateCapacity(removedCards, targetDeck);

        this.deckRepository.save(reserveDeck);
        this.deckRepository.save(addCardsToDeck(removedCards, targetDeck));
    }

    @Override
    @Transactional
    public void removeCards(Long userId, Long deckId, RemoveCardsRequest removeCardsRequest) {
        final var deck = this.deckRepository.findByIdAndPlayerId(deckId, userId).orElseThrow(DeckNotFound::new);
        final var cards = this.removeCardsFromDeck(deck, removeCardsRequest.ids());
        this.deckRepository.save(deck);
        final var spareDeck = this.deckRepository.findByPlayerIdAndName(userId, DeckConstants.RESERVE_DECK_NAME)
                .orElseThrow(DeckNotFound::new);
        spareDeck.getCards().addAll(cards);
        this.deckRepository.save(spareDeck);
    }

    @Override
    public DeckResponse rename(Long userId, Long deckId, RenameDeckRequest renameDeckRequest) {
        if (renameDeckRequest.name().equals(DeckConstants.RESERVE_DECK_NAME)) {
            throw new DeckNameForbidden();
        }

        return this.deckRepository
                .findByIdAndPlayerId(deckId, userId)
                .filter(deck -> !deck.getName().equals(DeckConstants.RESERVE_DECK_NAME))
                .map(deck -> this.deckMapper.map(renameDeckRequest, deck))
                .map(this.deckRepository::save)
                .map(this.deckMapper::map)
                .orElseThrow(DeckNotFound::new);
    }

    @Override
    public Page<DeckResponse> getAllByUserId(Long userId, Pageable pageable) {
        return this.deckRepository.findAll(
                        DeckSpecification.of(DeckConstants.NAME,
                                        OperationConstants.NOT_EQUAL,
                                        DeckConstants.RESERVE_DECK_NAME)
                                .and(DeckSpecification.of(DeckConstants.PLAYER_ID,
                                        OperationConstants.EQUAL,
                                        userId
                                )),
                        pageable
                )
                .map(this.deckMapper::map);
    }

    @KafkaListener(
            topics = TopicConstants.TOPIC_DECK_CREATION,
            containerFactory = KafkaConstants.KAFKA_CONSUMER_FACTORY
    )
    @Transactional
    public void register(UserIdEvent userIdEvent) {
        final var reserve = this.create(
                DeckConstants.RESERVE_DECK_NAME,
                userIdEvent.id(),
                null
        );

        final var first = this.create(
                DeckConstants.DECK_NAME_PREFIX + DeckConstants.FIRST_DECK_POSTFIX,
                userIdEvent.id(),
                DeckConstants.FIRST_DECK_SIZE
        );
        this.addAllCards(first);

        final var second = this.create(
                DeckConstants.DECK_NAME_PREFIX + DeckConstants.SECOND_DECK_POSTFIX,
                userIdEvent.id(),
                DeckConstants.SECOND_DECK_SIZE
        );
        this.addAllCards(second);

        this.deckRepository.save(reserve);
        this.deckRepository.save(first);
        this.deckRepository.save(second);
    }

    private Deck create(String name, Long playerId, Integer capacity) {
        final var deck = new Deck();
        deck.setName(name);
        deck.setPlayerId(playerId);
        deck.setCards(new ArrayList<>());
        deck.setCapacity(capacity);

        return deck;
    }

    private void addAllCards(Deck deck) {
        this.addCards(deck, Rarity.COMMON, DeckConstants.COMMON_CARDS_PERCENT);
        this.addCards(deck, Rarity.RARE, DeckConstants.RARE_CARDS_PERCENT);
        this.addCards(deck, Rarity.EPIC, DeckConstants.EPIC_CARDS_PERCENT);
    }

    private void addCards(Deck deck, Rarity rarity, double rarityPercent) {
        final var targetSize = (int) (deck.getCapacity() * rarityPercent) + deck.getCards().size();
        final var targetCards = this.cardRepository.findCardsByRarity(rarity);
        final var random = new Random();

        if (targetCards.size() < targetSize - deck.getCards().size()) {
            throw new CanNotCreateDeck();
        }

        while (deck.getCards().size() < targetSize) {
            final var card = targetCards.get(random.nextInt(targetCards.size()));
            if (!deck.getCards().contains(card)) {
                deck.getCards().add(card);
            }
        }
    }

    private Deck findPlayerReserveDeck(Long userId) {
        return this.deckRepository
                .findByPlayerIdAndName(userId,
                        DeckConstants.RESERVE_DECK_NAME
                )
                .orElseThrow(DeckNotFound::new);
    }

    private Deck findTargetDeckByDeckIdAndPlayer(Long userId, Long targetDeckId) {
        return this.deckRepository.findByIdAndPlayerId(targetDeckId, userId)
                .orElseThrow(DeckNotFound::new);
    }

    private List<Card> removeCardsFromDeck(Deck deck, List<Long> ids) {
        final var cards = new ArrayList<Card>();
        final var cardIterator = deck.getCards().iterator();
        while (cardIterator.hasNext() && ids.size() != 0) {
            final var card = cardIterator.next();
            if (ids.contains(card.getId())) {
                cards.add(card);
                cardIterator.remove();
                ids.remove(card.getId());
            }
        }
        if (!ids.isEmpty()) {
            throw new NotEnoughCardsInDeck();
        }

        return cards;
    }

    private void validateCapacity(List<Card> removedCards, Deck targetDeck) {
        if (
                Optional.ofNullable(
                                targetDeck.getCards()).map(List::size
                        )
                        .orElse(DeckConstants.ZERO_INDEX)
                        + removedCards.size() > targetDeck.getCapacity()
        ) {
            throw new DeckOverSizeLimitException();
        }
    }

    private Deck addCardsToDeck(List<Card> input, Deck target) {
        if (target.getCards() == null) {
            target.setCards(input);

            return target;
        }
        target.getCards().addAll(input);

        return target;
    }
}