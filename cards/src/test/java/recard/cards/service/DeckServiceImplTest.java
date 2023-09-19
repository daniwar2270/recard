package recard.cards.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import recard.cards.constant.DeckConstants;
import recard.cards.exception.CanNotCreateDeck;
import recard.cards.exception.DeckNotFound;
import recard.cards.exception.DeckOverSizeLimitException;
import recard.cards.exception.DeckNameForbidden;
import recard.cards.mapper.DeckMapper;
import recard.cards.exception.NotEnoughCardsInDeck;
import recard.cards.model.entity.Card;
import recard.cards.model.entity.Deck;
import recard.cards.model.enums.Rarity;
import recard.cards.model.payload.request.RenameDeckRequest;
import recard.cards.model.payload.response.DeckResponse;
import recard.cards.model.payload.request.RemoveCardsRequest;
import recard.cards.model.payload.response.events.UserIdEvent;
import recard.cards.repository.CardRepository;
import recard.cards.repository.DeckRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeckServiceImplTest {

    private static Long userId;
    private static List<Long> cardIds;
    private static List<Card> cards;
    private static Deck deck;
    private static Deck reserveDeck;
    private static Deck mappedDeck;
    private static Card card1;
    private static Card card2;
    private static Card card3;
    private static UserIdEvent userIdEvent;
    private static RemoveCardsRequest removeCardsRequest;
    private static RenameDeckRequest renameDeckRequest;

    @Mock
    private DeckRepository deckRepository;
    @Mock
    private DeckMapper deckMapper;
    @Mock
    private CardRepository cardRepository;
    @Captor
    private ArgumentCaptor<Deck> deckCaptor;
    @InjectMocks
    private DeckServiceImpl deckService;

    @BeforeEach
    void setUp() {
        userIdEvent = new UserIdEvent(1L);
        userId = 1L;

        cardIds = new ArrayList<>();
        cards = new ArrayList<>();

        deck = new Deck();
        deck.setName("Old name");
        deck.setId(2L);
        deck.setPlayerId(userIdEvent.id());
        deck.setCapacity(30);

        reserveDeck = new Deck();
        reserveDeck.setName(DeckConstants.RESERVE_DECK_NAME);

        card1 = new Card();
        card1.setId(0L);
        card2 = new Card();
        card2.setId(1L);
        card3 = new Card();
        card3.setId(1L);

        renameDeckRequest = new RenameDeckRequest(DeckConstants.RESERVE_DECK_NAME);

        mappedDeck = new Deck();
        mappedDeck.setName("New name");
        mappedDeck.setId(2L);
        mappedDeck.setPlayerId(userIdEvent.id());
        mappedDeck.setCapacity(30);
    }

    @Test
    void moveCardsToDeck_validData_cardsMovedSuccessfully() {
        cards.add(card1);
        cards.add(card2);
        reserveDeck.setCards(cards);
        deck.setCards(new ArrayList<>());
        deck.setCapacity(DeckConstants.CAPACITY_SIZE_MAX);
        cardIds.add(0L);
        cardIds.add(1L);

        when(this.deckRepository.findByPlayerIdAndName(userId, DeckConstants.RESERVE_DECK_NAME))
                .thenReturn(Optional.of(reserveDeck));
        when(this.deckRepository.findByIdAndPlayerId(deck.getId(), userId))
                .thenReturn(Optional.of(deck));

        this.deckService.moveCardsToDeck(userId, deck.getId(), cardIds);

        verify(this.deckRepository).findByPlayerIdAndName(userId, DeckConstants.RESERVE_DECK_NAME);
        verify(this.deckRepository).findByIdAndPlayerId(deck.getId(), userId);
        verify(this.deckRepository).save(reserveDeck);
        verify(this.deckRepository).save(deck);
    }

    @Test
    void moveCardsToDeck_deckOverSizeLimit_exceptionThrown() {
        cards.add(card1);
        cards.add(card2);
        reserveDeck.setCards(cards);
        deck.setCapacity(1);
        cardIds.add(0L);
        cardIds.add(1L);
        when(this.deckRepository.findByPlayerIdAndName(userId, DeckConstants.RESERVE_DECK_NAME))
                .thenReturn(Optional.of(reserveDeck));
        when(this.deckRepository.findByIdAndPlayerId(deck.getId(), userId))
                .thenReturn(Optional.of(deck));

        assertThrows(DeckOverSizeLimitException.class,
                () -> this.deckService.moveCardsToDeck(userId, deck.getId(), cardIds));
    }

    @Test
    void moveCardsToDeck_notEnoughCardsInDeck_exceptionThrown() {
        reserveDeck.setCards(cards);
        cardIds.add(1L);
        when(this.deckRepository.findByPlayerIdAndName(userId, DeckConstants.RESERVE_DECK_NAME))
                .thenReturn(Optional.of(reserveDeck));
        when(this.deckRepository.findByIdAndPlayerId(deck.getId(), userId))
                .thenReturn(Optional.of(deck));

        assertThrows(NotEnoughCardsInDeck.class,
                () -> this.deckService.moveCardsToDeck(userId, deck.getId(), cardIds)
        );
    }

    @Test
    void moveCardsToDeck_deckNotFound_exceptionThrown() {
        when(this.deckRepository.findByPlayerIdAndName(userId, DeckConstants.RESERVE_DECK_NAME))
                .thenReturn(Optional.empty());

        assertThrows(DeckNotFound.class,
                () -> this.deckService.moveCardsToDeck(userId, deck.getId(), cardIds)
        );

    }

    @Test
    void createDecks_expectDeckNotCreate() {
        assertThrows(CanNotCreateDeck.class, () -> this.deckService.register(userIdEvent));
    }

    @Test
    void createDecks_expectSuccessfulDecksCreation() {
        final var commonCards = this.generateCards(60, 0L, Rarity.COMMON);
        final var rareCards = this.generateCards(30, 61L, Rarity.RARE);
        final var epicCards = this.generateCards(10, 91L, Rarity.EPIC);

        when(this.cardRepository.findCardsByRarity(eq(Rarity.COMMON))).thenReturn(commonCards);
        when(this.cardRepository.findCardsByRarity(eq(Rarity.RARE))).thenReturn(rareCards);
        when(this.cardRepository.findCardsByRarity(eq(Rarity.EPIC))).thenReturn(epicCards);

        this.deckService.register(userIdEvent);

        verify(this.deckRepository, times(3)).save(this.deckCaptor.capture());

        final var resultDecks = this.deckCaptor.getAllValues();
        final var reserve = resultDecks.get(0);
        final var first = resultDecks.get(1);
        final var second = resultDecks.get(2);

        assertAll(
                () -> assertEquals(DeckConstants.RESERVE_DECK_NAME, reserve.getName()),
                () -> assertEquals(userIdEvent.id(), reserve.getPlayerId()),
                () -> assertNull(reserve.getCapacity()),
                () -> assertTrue(reserve.getCards().isEmpty()),

                () -> assertEquals(
                        DeckConstants.DECK_NAME_PREFIX + DeckConstants.FIRST_DECK_POSTFIX,
                        first.getName()
                ),
                () -> assertEquals(userIdEvent.id(), first.getPlayerId()),
                () -> assertEquals(DeckConstants.FIRST_DECK_SIZE, first.getCapacity()),
                () -> assertEquals(
                        (int) (DeckConstants.COMMON_CARDS_PERCENT * DeckConstants.FIRST_DECK_SIZE),
                        this.getCountByRarity(first.getCards(), Rarity.COMMON)
                ),
                () -> assertEquals(
                        (int) (DeckConstants.RARE_CARDS_PERCENT * DeckConstants.FIRST_DECK_SIZE),
                        this.getCountByRarity(first.getCards(), Rarity.RARE)
                ),
                () -> assertEquals(
                        (int) (DeckConstants.EPIC_CARDS_PERCENT * DeckConstants.FIRST_DECK_SIZE),
                        this.getCountByRarity(first.getCards(), Rarity.EPIC)
                ),

                () -> assertEquals(
                        DeckConstants.DECK_NAME_PREFIX + DeckConstants.SECOND_DECK_POSTFIX,
                        second.getName()
                ),
                () -> assertEquals(userIdEvent.id(), second.getPlayerId()),
                () -> assertEquals(DeckConstants.SECOND_DECK_SIZE, second.getCapacity()),
                () -> assertEquals(
                        (int) (DeckConstants.COMMON_CARDS_PERCENT * DeckConstants.SECOND_DECK_SIZE),
                        this.getCountByRarity(second.getCards(), Rarity.COMMON)
                ),
                () -> assertEquals(
                        (int) (DeckConstants.RARE_CARDS_PERCENT * DeckConstants.SECOND_DECK_SIZE),
                        this.getCountByRarity(second.getCards(), Rarity.RARE)
                ),
                () -> assertEquals(
                        (int) (DeckConstants.EPIC_CARDS_PERCENT * DeckConstants.SECOND_DECK_SIZE),
                        this.getCountByRarity(second.getCards(), Rarity.EPIC)
                )
        );
    }

    @Test
    void rename_expectDeckNameForbidden() {
        assertThrows(
                DeckNameForbidden.class,
                () -> this.deckService.rename(userIdEvent.id(), deck.getId(), renameDeckRequest)
        );
    }

    @Test
    void rename_expectDeckNotFound() {
        renameDeckRequest = new RenameDeckRequest(mappedDeck.getName());
        when(this.deckRepository.findByIdAndPlayerId(deck.getId(), deck.getPlayerId()))
                .thenReturn(Optional.empty());
        assertThrows(
                DeckNotFound.class,
                () -> this.deckService.rename(deck.getPlayerId(), deck.getId(), renameDeckRequest))
        ;
    }

    @Test
    void rename_expectRenameDeck() {
        renameDeckRequest = new RenameDeckRequest(mappedDeck.getName());
        when(this.deckRepository.findByIdAndPlayerId(deck.getId(), deck.getPlayerId()))
                .thenReturn(Optional.of(deck));
        when(this.deckRepository.save(ArgumentMatchers.any()))
                .thenReturn(mappedDeck);
        when(this.deckMapper.map(renameDeckRequest, deck))
                .thenReturn(mappedDeck);
        when(this.deckMapper.map(mappedDeck)).thenReturn(new DeckResponse(
                mappedDeck.getId(),
                mappedDeck.getPlayerId(),
                mappedDeck.getName(),
                mappedDeck.getCapacity())
        );

        final var result = this.deckService.rename(deck.getPlayerId(), deck.getId(), renameDeckRequest);

        verify(this.deckRepository).findByIdAndPlayerId(deck.getId(), deck.getPlayerId());
        verify(this.deckRepository).save(ArgumentMatchers.any());
        verify(this.deckMapper).map(renameDeckRequest, deck);
        verify(this.deckMapper).map(mappedDeck);

        assertAll(
                () -> assertEquals(deck.getId(), result.id()),
                () -> assertEquals(deck.getPlayerId(), result.playerId()),
                () -> assertEquals(mappedDeck.getName(), result.name()),
                () -> assertEquals(deck.getCapacity(), result.capacity())
        );
    }

    @Test
    public void removeCardsFromDeck_expectCardsRemoved() {
        cards.add(card1);
        cards.add(card2);
        deck.setCards(cards);
        reserveDeck.setCards(new ArrayList<>());
        cardIds.add(0L);
        cardIds.add(1L);
        removeCardsRequest = new RemoveCardsRequest(cardIds);

        when(this.deckRepository.findByIdAndPlayerId(deck.getId(), userId))
                .thenReturn(Optional.ofNullable(deck));

        when(this.deckRepository.findByPlayerIdAndName(userId, DeckConstants.RESERVE_DECK_NAME))
                .thenReturn(Optional.ofNullable(reserveDeck));

        this.deckService.removeCards(userId, deck.getId(), removeCardsRequest);

        verify(this.deckRepository, times(2)).save(this.deckCaptor.capture());

        final var resultDecks = this.deckCaptor.getAllValues();
        final var captureDeck = resultDecks.get(0);
        final var captureReserveDeck = resultDecks.get(1);

        final var removedCards = captureDeck.getCards();
        removedCards.removeAll(captureReserveDeck.getCards());

        assertAll(
                () -> assertTrue(cardIds.isEmpty()),
                () -> assertTrue(captureDeck.getCards().isEmpty()),
                () -> assertEquals(2, captureReserveDeck.getCards().size()),
                () -> assertEquals(deck.getId(), captureDeck.getId())
        );
    }

    @Test
    public void removeCardsFromDeck_expectNotEnoughCardsInDeck() {
        cards.add(card1);
        cards.add(card2);
        deck.setCards(cards);
        reserveDeck.setCards(new ArrayList<>());
        cardIds.add(0L);
        cardIds.add(1L);
        cardIds.add(1L);
        removeCardsRequest = new RemoveCardsRequest(cardIds);

        when(this.deckRepository.findByIdAndPlayerId(deck.getId(), userId))
                .thenReturn(Optional.ofNullable(deck));

        assertThrows(NotEnoughCardsInDeck.class,
                () -> this.deckService.removeCards(userId, deck.getId(), removeCardsRequest));
    }

    @Test
    public void getAllByUserId_OnExistingDecks_expectedPageOfDecks() {
        final var pageable = Pageable.ofSize(10).withPage(0);
        deck.setId(1L);
        deck.setCapacity(10);
        deck.setCards(List.of(new Card(), new Card()));

        final var deckPage = new PageImpl<>(List.of(deck), pageable, 1);

        when(this.deckRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(deckPage);
        when(this.deckMapper.map(deck))
                .thenReturn(new DeckResponse(deck.getId(), userId, "Deck", deck.getCapacity()));

        final var result = this.deckService.getAllByUserId(userId, pageable);
        final var firstDeck = result.getContent().get(0);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(deck.getId(), firstDeck.id()),
                () -> assertEquals(10, firstDeck.capacity())
        );
        verify(this.deckMapper).map(deck);
        verify(this.deckRepository).findAll(any(Specification.class), eq(pageable));
    }

    private List<Card> generateCards(int count, Long startIndex, Rarity rarity) {
        return IntStream.range(0, count)
                .mapToObj(i -> {
                    final var card = new Card();
                    card.setId(i + startIndex);
                    card.setRarity(rarity);
                    return card;
                })
                .toList();
    }

    private Long getCountByRarity(List<Card> cards, Rarity rarity) {
        return cards
                .stream()
                .filter(card -> card.getRarity() == rarity)
                .count();
    }
}