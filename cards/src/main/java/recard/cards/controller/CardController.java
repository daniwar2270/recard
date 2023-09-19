package recard.cards.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import recard.cards.constant.HeaderConstants;
import recard.cards.model.entity.Card;
import recard.cards.model.payload.response.CardResponse;
import recard.cards.service.CardService;

@RestController
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CardResponse get(@PathVariable Long id) {
        return this.cardService.getById(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<CardResponse> get(Specification<Card> specification, Pageable pageable) {
        return cardService.getCards(specification, pageable);
    }

    @GetMapping("/player")
    @ResponseStatus(HttpStatus.OK)
    public Page<CardResponse> getByUserId(
            @RequestHeader(HeaderConstants.USER_ID) Long userId,
            String availability,
            Specification<Card> specification,
            Pageable pageable
    ) {
        return this.cardService.getCardsByUserId(userId, specification, pageable, availability);
    }

    @GetMapping("/player/decks/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<CardResponse> getByDeckId(
            @RequestHeader(HeaderConstants.USER_ID) Long userId,
            @PathVariable Long deckId,
            Specification<Card> specification,
            Pageable pageable
    ) {
        return this.cardService.getCardsByDeckId(deckId, userId, specification, pageable);
    }
}