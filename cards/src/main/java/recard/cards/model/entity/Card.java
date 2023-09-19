package recard.cards.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import recard.cards.model.enums.Rarity;

import java.util.List;

@Entity
@Table(name = "cards")
@Data
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Rarity rarity;

    private Integer attack;

    private Integer defence;

    private String imageUrl;

    private String thumbnailUrl;

    @ManyToMany(mappedBy = "cards")
    private List<Deck> decks;
}