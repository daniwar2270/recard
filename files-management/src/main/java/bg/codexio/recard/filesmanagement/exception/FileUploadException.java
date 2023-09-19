package bg.codexio.recard.filesmanagement.exception;

import bg.codexio.recard.filesmanagement.constant.FileErrorConstants;

public class FileUploadException extends RuntimeException {

    public FileUploadException() {
        super(FileErrorConstants.FILE_UPLOAD_FAILURE);
    }
}