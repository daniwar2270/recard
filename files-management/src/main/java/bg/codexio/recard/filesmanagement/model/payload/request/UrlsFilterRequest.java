package bg.codexio.recard.filesmanagement.model.payload.request;

import bg.codexio.recard.filesmanagement.config.KafkaPayload;

import java.util.Set;

@KafkaPayload
public record UrlsFilterRequest(Set<String> imageUrls, Set<String> thumbnailUrls) {
}