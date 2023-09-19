package bg.codexio.recard.filesmanagement.service;

import bg.codexio.recard.filesmanagement.config.ImageConfig;
import bg.codexio.recard.filesmanagement.constant.*;
import bg.codexio.recard.filesmanagement.exception.NonGraphicalFileException;
import bg.codexio.recard.filesmanagement.exception.FileUploadException;
import bg.codexio.recard.filesmanagement.model.payload.request.UrlsFilterRequest;
import bg.codexio.recard.filesmanagement.model.payload.event.ImageUploadEvent;
import bg.codexio.recard.filesmanagement.model.payload.response.ImageUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.imgscalr.Scalr;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final ImageConfig imageConfig;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @SneakyThrows
    @Override
    public ImageUploadResponse upload(MultipartFile file, String userId) {
        this.validate(file);

        final var timestamp = UUID.randomUUID()
                .toString()
                .substring(
                        IndexConstants.START_INDEX_UUID_SUBSTRING,
                        IndexConstants.END_INDEX_UUID_SUBSTRING) + SingleSymbolConstants.UNDERLINE + (
                Instant.now()
                        .toEpochMilli()
        );
        final var extension = this.getExtension(file);
        final var path = this.generatePath(timestamp, extension);
        final var thumbnailPath = this.generatePath(
                timestamp + FileConstants.FILE_THUMBNAIL_SUFFIX,
                extension
        );

        Optional.ofNullable(ImageIO.read(file.getInputStream()))
                .map(this::compressImage)
                .map(compressedImage -> this.saveImage(
                                compressedImage,
                                extension,
                                path
                        )
                )
                .map(this::generateThumbnail)
                .map(thumbnail -> this.saveThumbnail(
                                thumbnail,
                                extension,
                                thumbnailPath
                        )
                )
                .orElseThrow(FileUploadException::new);

        final var imageUploadResponse = this.generateUrl(path.getFileName().toString());
        final var thumbnailUploadResponse = this.generateUrl(thumbnailPath.getFileName().toString());

        this.publishUserDetails(userId, imageUploadResponse.imageUrl(), thumbnailUploadResponse.imageUrl());

        return imageUploadResponse;
    }

    @SneakyThrows
    private void validate(MultipartFile file) {
        Optional.ofNullable(ImageIO.read(file.getInputStream()))
                .orElseThrow(NonGraphicalFileException::new);
    }

    private String getExtension(MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename())
                .substring(
                        file.getOriginalFilename()
                                .lastIndexOf(SingleSymbolConstants.DOT));
    }

    private Path generatePath(String timestamp, String extension) {
        return Paths.get(this.imageConfig.getDir(), timestamp + extension);
    }

    @SneakyThrows
    private BufferedImage compressImage(BufferedImage img) {
        return Scalr
                .resize(
                        img,
                        Scalr.Method.QUALITY,
                        Scalr.Mode.AUTOMATIC,
                        img.getWidth() / this.imageConfig.getCompressionRatio(),
                        Scalr.OP_ANTIALIAS
                );
    }

    @SneakyThrows
    private BufferedImage saveImage(BufferedImage img, String extension, Path filePath) {
        Files.createDirectories(filePath.getParent());

        ImageIO.write(
                img,
                extension.replace(
                        SingleSymbolConstants.DOT,
                        SingleSymbolConstants.EMPTY_STRING),
                filePath.toFile()
        );

        return img;
    }

    @SneakyThrows
    private BufferedImage generateThumbnail(BufferedImage img) {
        return Scalr
                .resize(
                        img,
                        Scalr.Method.ULTRA_QUALITY,
                        Scalr.Mode.AUTOMATIC,
                        this.imageConfig
                                .getThumbnailSize(),
                        Scalr.OP_ANTIALIAS
                );
    }

    @SneakyThrows
    private BufferedImage saveThumbnail(BufferedImage thumbnail, String extension, Path filePath) {
        ImageIO.write(
                thumbnail,
                extension.replace(
                        SingleSymbolConstants.DOT,
                        SingleSymbolConstants.EMPTY_STRING),
                filePath.toFile()
        );

        return thumbnail;
    }

    private ImageUploadResponse generateUrl(String fileName) {
        return new ImageUploadResponse(this.imageConfig.getBaseUrl() + fileName);
    }

    private void publishUserDetails(String userId, String imageUrl, String thumbnailUrl) {
        final var userUploadImageRequest = new ImageUploadEvent(userId, imageUrl, thumbnailUrl);
        this.kafkaTemplate.send(TopicNamingService.getTopicName(ImageUploadEvent.class), userUploadImageRequest);
    }

    @KafkaListener(topics = TopicConstants.TOPICS_URL_FILTERING,
            containerFactory = KafkaConstants.KAFKA_CONSUMER_FACTORY)
    private void clearUnusedUrls(UrlsFilterRequest filterUrlsRequest) {
        final var dir = new File(this.imageConfig.getDir());
        Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .forEach(file -> {
                    final var relativePath = file.getPath().replace(File.separator, SingleSymbolConstants.BACKSLASH);
                    final var filePathToCheck = this.imageConfig.getBaseUrl() +
                            relativePath
                                    .replace(this.imageConfig.getDir() + SingleSymbolConstants.BACKSLASH,
                                            SingleSymbolConstants.EMPTY_STRING);

                    final var isThumbnail = filePathToCheck.contains(FileConstants.FILE_THUMBNAIL_SUFFIX);
                    final var isThumbnailNotPresent = !filterUrlsRequest.thumbnailUrls().contains(filePathToCheck);
                    final var isImageNotPresent = !filterUrlsRequest.imageUrls().contains(filePathToCheck);

                    if ((isThumbnail && isThumbnailNotPresent) || (!isThumbnail && isImageNotPresent)) {
                        file.delete();
                    }
                });
    }
}