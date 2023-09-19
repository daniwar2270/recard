package recard.cards.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import recard.cards.model.entity.Card;
import recard.cards.model.payload.response.CardResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardMapper {

    CardResponse map(Card card);
}