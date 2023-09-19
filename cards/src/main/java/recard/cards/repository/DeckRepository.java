package recard.cards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import recard.cards.model.entity.Deck;

import java.util.Optional;

public interface DeckRepository extends JpaRepository<Deck, Long>, JpaSpecificationExecutor<Deck> {

    Optional<Deck> findByPlayerIdAndName(Long id, String name);

    Optional<Deck> findByIdAndPlayerId(Long id, Long playerId);
}