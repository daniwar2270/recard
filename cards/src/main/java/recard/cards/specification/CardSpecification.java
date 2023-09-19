package recard.cards.specification;

import org.springframework.data.jpa.domain.Specification;
import recard.cards.constant.CardConstants;
import recard.cards.constant.OperationConstants;
import recard.cards.constant.SingleSymbolConstants;
import recard.cards.model.entity.Card;

public class CardSpecification {

    public static Specification<Card> of(String fieldName, String operation, String value) {
        return (root, query, criteriaBuilder) -> switch (operation) {
            case OperationConstants.CONTAINS -> criteriaBuilder.like(criteriaBuilder.lower(root.get(fieldName)),
                    SingleSymbolConstants.PERCENT + value + SingleSymbolConstants.PERCENT);
            case OperationConstants.LESS_THAN -> criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), value);
            case OperationConstants.GREATER_THAN -> criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), value);
            default -> null;
        };
    }

    public static <T> Specification<Card> ofDeckField(String fieldName, String operation, T value) {
        return (root, query, criteriaBuilder) -> switch (operation) {
            case OperationConstants.EQUAL -> criteriaBuilder
                    .equal(root.get(CardConstants.DECKS)
                            .get(fieldName), value);
            case OperationConstants.NOT_EQUAL -> criteriaBuilder
                    .notEqual(root.get(CardConstants.DECKS)
                            .get(fieldName), value);
            default -> null;
        };
    }
}