package recard.cards.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import recard.cards.constant.ErrorConstants;
import recard.cards.constant.SingleSymbolConstants;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DeckOverSizeLimitException.class)
    public ResponseEntity<String> handle(DeckOverSizeLimitException ex) {
        log.error(ex.getMessage());
        log.info(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.DECK_OVER_SIZE_LIMIT);
    }

    @ExceptionHandler(CardNotFound.class)
    public ResponseEntity<String> handle(CardNotFound ex) {
        log.error(ex.getMessage());
        log.info(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.CARD_NOT_FOUND);
    }

    @ExceptionHandler(DeckNotFound.class)
    public ResponseEntity<String> handle(DeckNotFound ex) {
        log.error(ex.getMessage());
        log.info(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.DECK_NOT_FOUND);
    }

    @ExceptionHandler(DeckNameForbidden.class)
    public ResponseEntity<String> handle(DeckNameForbidden ex) {
        log.error(ex.getMessage());
        log.info(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorConstants.DECK_NAME_FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        log.info(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex
                        .getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.joining(SingleSymbolConstants.SPACE))
                );
    }

    @ExceptionHandler(CanNotCreateDeck.class)
    public ResponseEntity<String> handle(CanNotCreateDeck ex) {
        log.error(ex.getMessage());
        log.info(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorConstants.DECK_NOT_AVAILABLE);
    }

    @ExceptionHandler(NotEnoughCardsInDeck.class)
    public ResponseEntity<String> handle(NotEnoughCardsInDeck ex) {
        log.error(ex.getMessage());
        log.info(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorConstants.NOT_ENOUGH_CARDS_DECK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception ex) {
        log.error(ex.getMessage());
        log.info(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorConstants.INTERNAL_SERVER_ERROR);
    }
}