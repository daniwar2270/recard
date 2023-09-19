package recard.cards.model.searchcriteria;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SearchCriteria {

    private String fieldName;
    private String operation;
    private String value;
}