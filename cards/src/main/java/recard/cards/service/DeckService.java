package recard.cards.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import recard.cards.model.payload.request.RemoveCardsRequest;
import recard.cards.model.payload.request.RenameDeckRequest;
import recard.cards.model.payload.response.DeckResponse;

import java.util.List;

public interface DeckService {

    Page<DeckResponse> getAllByUserId(Long id, Pageable pageable);

    void moveCardsToDeck (Long userId, Long targetDeckId, List<Long> cardIds);

    void removeCards(Long userId, Long deckId, RemoveCardsRequest removeCardsRequest);

    DeckResponse rename(Long userId, Long deckId, RenameDeckRequest renameDeckRequest);
}