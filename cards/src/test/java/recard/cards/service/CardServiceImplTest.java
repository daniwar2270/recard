package recard.cards.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import recard.cards.constant.OperationConstants;
import recard.cards.exception.CardNotFound;
import recard.cards.mapper.CardMapper;
import recard.cards.model.entity.Card;
import recard.cards.model.entity.Deck;
import recard.cards.model.enums.Rarity;
import recard.cards.model.payload.response.CardResponse;
import recard.cards.repository.CardRepository;
import recard.cards.specification.CardSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    private static Card card;
    private static CardResponse cardResponse;
    private static List<Card> cards;
    private static List<CardResponse> cardResponses;
    private static Deck deck;
    private static Specification<Card> spec;
    private static Pageable pageable;

    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardMapper cardMapper;
    @Captor
    private ArgumentCaptor<Card> cardCaptor;
    @Captor
    private ArgumentCaptor<Specification<Card>> specCaptor;
    @InjectMocks
    private CardServiceImpl cardService;

    @BeforeEach
    void setUp() {
        card = new Card();
        card.setId(1L);
        card.setName("Card");
        card.setDescription("Description");
        card.setAttack(10);
        card.setDefence(10);
        card.setRarity(Rarity.COMMON);
        card.setImageUrl("imageUrl");
        card.setThumbnailUrl("thumbnailUrl");

        cardResponse = new CardResponse(
                1L,
                "Card",
                "Description",
                10,
                10,
                "imageUrl",
                Rarity.COMMON.toString()
        );

        cards = new ArrayList<>();
        cards.add(card);
        cards.add(card);

        cardResponses = new ArrayList<>();
        cardResponses.add(cardResponse);
        cardResponses.add(cardResponse);

        deck = new Deck();
        deck.setName("Deck");
        deck.setId(1L);
        deck.setPlayerId(1L);

        spec = CardSpecification.of("name", OperationConstants.CONTAINS, card.getName());

        pageable = Pageable.unpaged();
    }

    @Test
    void getById_onExistingCard_expectedCardFound() {
        when(this.cardRepository.findById(card.getId())).thenReturn(Optional.of(card));
        when(this.cardMapper.map(card)).thenReturn(cardResponse);

        final var result = this.cardService.getById(card.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(card.getId(), result.id()),
                () -> assertEquals(card.getName(), result.name()),
                () -> assertEquals(card.getDescription(), result.description()),
                () -> assertEquals(card.getAttack(), result.attack()),
                () -> assertEquals(card.getDefence(), result.defence()),
                () -> assertEquals(card.getImageUrl(), result.imageUrl()),
                () -> assertEquals(card.getRarity().toString(), result.rarity())
        );
        verify(this.cardRepository).findById(card.getId());
        verify(this.cardMapper).map(card);
    }

    @Test
    void getById_onNotExistingCard_expectedCardNotFound() {
        when(this.cardRepository.findById(card.getId())).thenReturn(Optional.empty());

        assertThrows(CardNotFound.class, () -> this.cardService.getById(card.getId()));
        verify(this.cardRepository).findById(card.getId());
    }

    @Test
    void getCardsByDeckId_expectGetDeck() {
        final var cardPage = new PageImpl<>(cards, pageable, cards.size());

        when(cardRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(cardPage);
        when(this.cardMapper.map(eq(card))).thenReturn(cardResponse);

        final var result = this.cardService.getCardsByDeckId(
                deck.getId(),
                deck.getPlayerId(),
                spec,
                pageable
        );

        verify(cardRepository).findAll(specCaptor.capture(), eq(pageable));
        verify(cardMapper, times(cards.size())).map(cardCaptor.capture());

        assertAll(
                () -> assertEquals(cardResponses, result.getContent()),
                () -> assertEquals(cards, cardCaptor.getAllValues())
        );
    }

    @Test
    void getCards_expectGetCards() {
        final var cardPage = new PageImpl<>(cards, pageable, cards.size());

        when(cardRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(cardPage);
        when(this.cardMapper.map(eq(card))).thenReturn(cardResponse);

        final var result = this.cardService.getCards(
                spec,
                pageable
        );

        verify(cardRepository).findAll(specCaptor.capture(), eq(pageable));
        verify(cardMapper, times(cards.size())).map(cardCaptor.capture());

        assertAll(
                () -> assertEquals(cardResponses, result.getContent()),
                () -> assertEquals(cards, cardCaptor.getAllValues())
        );
    }

    @Test
    public void getCardsById_OnExistingCards_expectedPageOfCards() {
        final var cards = new ArrayList<>();
        cards.add(card);
        final var userId = 1L;
        final var cardPage = new PageImpl<>(cards, pageable, cards.size());
        Specification<Card> specification = Specification
                .where((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), ""));

        when(this.cardRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(cardPage);
        when(this.cardMapper.map(eq(card))).thenReturn(cardResponse);

        final var result = this.cardService.getCardsByUserId(
                userId,
                specification,
                pageable,
                null
        );

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(cardResponse, result.getContent().get(0))
        );
        verify(this.cardMapper).map(card);
    }
}