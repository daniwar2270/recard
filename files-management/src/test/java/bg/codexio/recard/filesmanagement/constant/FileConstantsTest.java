package bg.codexio.recard.filesmanagement.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileConstantsTest {

    public static final String FILE_DEFAULT_NAME = "file";
    public static final String FILE_DEFAULT_ORIGINAL_NAME = "test.jpg";
    public static final String FILE_DEFAULT_PATH = "src/test/resources/image/test.jpg";
    public static final String FILE_DEFAULT_CONTENT_TYPE = "image/jpeg";
    public static final String FILE_BASE_DIR = "/path/to/images";
    public static final String FILE_BASE_URL = "http://localhost/images/";
    public static final String FILE_USER_ID = "userId";
    public static final Integer FILE_THUMBNAIL_SIZE = 150;
    public static final Integer FILE_COMPRESS_RATIO = 2;
    public static final Integer FILE_START_INDEX = 0;
}