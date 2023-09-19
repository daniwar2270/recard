package bg.codexio.recard.filesmanagement.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileErrorConstants {

    public static final String FILE_UPLOAD_FAILURE = "File upload failed.";
    public static final String FILE_TOO_LARGE = "Image cannot exceed %s.";
    public static final String INVALID_FILE_TYPE = "Uploaded file is not an image!";
}