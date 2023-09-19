package recard.cards.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import recard.cards.model.entity.Deck;
import recard.cards.model.payload.request.RenameDeckRequest;
import recard.cards.model.payload.response.DeckResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeckMapper {

    @Mapping(target = "capacity", expression = "java(deck.getCapacity() - deck.getCards().size())")
    DeckResponse map(Deck deck);

    Deck map(RenameDeckRequest renameDeckRequest, @MappingTarget Deck deck);
}