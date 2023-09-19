package bg.codexio.recard.filesmanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import bg.codexio.recard.filesmanagement.constant.FileConstantsTest;
import org.imgscalr.Scalr;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockMultipartFile;

import bg.codexio.recard.filesmanagement.config.ImageConfig;
import bg.codexio.recard.filesmanagement.model.payload.event.ImageUploadEvent;

import javax.imageio.ImageIO;

@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {

    @Mock
    private ImageConfig imageConfig;
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;
    @InjectMocks
    private FileServiceImpl fileService;

    @BeforeEach
    void setUp() {
        when(imageConfig.getDir()).thenReturn(FileConstantsTest.FILE_BASE_DIR);
        when(imageConfig.getBaseUrl()).thenReturn(FileConstantsTest.FILE_BASE_URL);
        when(imageConfig.getThumbnailSize()).thenReturn(FileConstantsTest.FILE_THUMBNAIL_SIZE);
        when(imageConfig.getCompressionRatio()).thenReturn(FileConstantsTest.FILE_COMPRESS_RATIO);
    }

    @Test
    void upload_validImage_returnsImageUploadResponse() {
        try {
            final var path = Paths.get(FileConstantsTest.FILE_DEFAULT_PATH);
            final var content = Files.readAllBytes(path);
            final var file = new MockMultipartFile(
                    FileConstantsTest.FILE_DEFAULT_NAME,
                    FileConstantsTest.FILE_DEFAULT_ORIGINAL_NAME,
                    FileConstantsTest.FILE_DEFAULT_CONTENT_TYPE, content
            );
            final var bufferedImage = ImageIO.read(file.getInputStream());

            final var compressedImage = Scalr
                    .resize(
                            bufferedImage,
                            Scalr.Method.QUALITY,
                            Scalr.Mode.AUTOMATIC,
                            bufferedImage.getWidth() / this.imageConfig.getCompressionRatio(),
                            Scalr.OP_ANTIALIAS
                    );
            final var response = this.fileService.upload(file, FileConstantsTest.FILE_USER_ID);

            assertAll(
                    () -> assertNotNull(response),
                    () -> assertEquals(
                            this.imageConfig.getBaseUrl(),
                            response.imageUrl().substring(
                                    FileConstantsTest.FILE_START_INDEX,
                                    this.imageConfig.getBaseUrl().length()
                            )
                    ),
                    () -> assertTrue(compressedImage.getWidth() < bufferedImage.getWidth()),
                    () -> assertTrue(compressedImage.getHeight() < bufferedImage.getHeight())
            );
        } catch (IOException e) {
        }
        verify(this.kafkaTemplate).send(anyString(), any(ImageUploadEvent.class));
    }
}