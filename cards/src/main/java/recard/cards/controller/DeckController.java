package recard.cards.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import recard.cards.constant.HeaderConstants;
import recard.cards.model.payload.request.RenameDeckRequest;
import recard.cards.model.payload.response.DeckResponse;
import recard.cards.model.payload.request.RemoveCardsRequest;
import recard.cards.model.payload.request.CardsToDeckRequest;
import recard.cards.service.DeckService;

@RestController
@RequiredArgsConstructor
public class DeckController {

    private final DeckService deckService;

    @PatchMapping("/decks/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeCards(@RequestHeader(HeaderConstants.USER_ID) Long userId,
                            @PathVariable Long deckId,
                            @RequestBody RemoveCardsRequest removeCardsRequest) {
        this.deckService.removeCards(userId, deckId, removeCardsRequest);
    }

    @PatchMapping("/decks/{deckId}/name")
    @ResponseStatus(HttpStatus.OK)
    public DeckResponse rename(
            @RequestHeader(HeaderConstants.USER_ID) Long userId,
            @PathVariable Long deckId,
            @Valid @RequestBody RenameDeckRequest renameDeckRequest
    ) {
        return this.deckService.rename(userId, deckId, renameDeckRequest);
    }

    @GetMapping("/decks")
    @ResponseStatus(HttpStatus.OK)
    public Page<DeckResponse> getByUserId(@RequestHeader(HeaderConstants.USER_ID) Long id, Pageable pageable) {
        return this.deckService.getAllByUserId(id, pageable);
    }

    @PutMapping("/decks/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    public void moveCards(
            @RequestHeader(HeaderConstants.USER_ID)
            Long userId,
            @PathVariable
            Long deckId,
            @RequestBody
            CardsToDeckRequest cardsToDeckRequest
    ) {
        this.deckService.moveCardsToDeck(userId, deckId, cardsToDeckRequest.cardIds());
    }
}