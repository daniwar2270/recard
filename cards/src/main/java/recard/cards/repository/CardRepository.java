package recard.cards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import recard.cards.model.entity.Card;
import recard.cards.model.enums.Rarity;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {

    List<Card> findCardsByRarity(Rarity rarity);
}