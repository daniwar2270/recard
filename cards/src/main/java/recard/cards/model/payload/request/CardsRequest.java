package recard.cards.model.payload.request;

import org.springframework.web.bind.annotation.RequestParam;

public record CardsRequest(

        @RequestParam(required = false)
        String name,
        @RequestParam(required = false)
        Integer minAttack,
        @RequestParam(required = false)
        Integer maxAttack,
        @RequestParam(required = false)
        Integer minDefence,
        @RequestParam(required = false)
        Integer maxDefence
) {
}