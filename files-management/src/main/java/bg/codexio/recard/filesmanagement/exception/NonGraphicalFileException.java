package bg.codexio.recard.filesmanagement.exception;

import bg.codexio.recard.filesmanagement.constant.FileErrorConstants;

public class NonGraphicalFileException extends RuntimeException {

    public NonGraphicalFileException() {
        super(FileErrorConstants.INVALID_FILE_TYPE);
    }
}