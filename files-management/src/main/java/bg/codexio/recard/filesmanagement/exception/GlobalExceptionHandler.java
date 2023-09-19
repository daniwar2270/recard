package bg.codexio.recard.filesmanagement.exception;

import bg.codexio.recard.filesmanagement.constant.FileErrorConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final String fileMaxSize;

    public GlobalExceptionHandler(@Value("${spring.servlet.multipart.max-file-size}") final String fileMaxSize) {
        this.fileMaxSize = fileMaxSize;
    }

    @ExceptionHandler(NonGraphicalFileException.class)
    public ResponseEntity<String> handle(NonGraphicalFileException ex) {
        log.error(ex.getMessage());
        log.info(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handle(IOException ex) {
        log.error(ex.getMessage());
        log.info(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<String> handle(SizeLimitExceededException ex) {
        log.error(ex.getMessage());
        log.info(ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE).body(
                        String.format(
                                FileErrorConstants
                                        .FILE_TOO_LARGE, this.fileMaxSize
                        ));
    }
}