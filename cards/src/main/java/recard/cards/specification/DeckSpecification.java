package recard.cards.specification;

import org.springframework.data.jpa.domain.Specification;
import recard.cards.constant.OperationConstants;
import recard.cards.model.entity.Deck;

public class DeckSpecification {

    public static <T> Specification<Deck> of(String fieldName, String operation, T value) {
        return (root, query, criteriaBuilder) -> switch (operation) {
            case OperationConstants.NOT_EQUAL -> criteriaBuilder.notEqual(root.get(fieldName),
                    value);
            case OperationConstants.EQUAL -> criteriaBuilder.equal(root.get(fieldName), value);
            default -> null;
        };
    }
}