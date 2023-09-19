package recard.cards.argumentresolver;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import recard.cards.constant.RegexConstants;
import recard.cards.constant.FilterConstants;
import recard.cards.constant.SingleSymbolConstants;
import recard.cards.model.entity.Card;
import recard.cards.specification.CardSpecification;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Specification.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        final var filterJson = webRequest.getParameterMap().get(FilterConstants.SEARCH_CRITERIA);
        final var filterList = filterJson == null ? new String[0] : filterJson;

        return createSpecification(Arrays.asList(filterList));
    }

    private Specification<Card> createSpecification(List<String> searchCriteriaList) {
        return searchCriteriaList.stream()
                .map(criteria -> criteria.replaceAll(RegexConstants.CRITERIA_REGEX, ""))
                .map(values -> values.split(SingleSymbolConstants.COMMA))
                .map(values -> CardSpecification.of(values[0], values[1], values[2]))
                .reduce(Specification::and)
                .orElse(null);
    }
}