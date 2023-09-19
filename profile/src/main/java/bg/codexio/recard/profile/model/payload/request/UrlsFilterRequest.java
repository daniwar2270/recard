package bg.codexio.recard.profile.model.payload.request;

import bg.codexio.recard.profile.annotation.KafkaPayload;

import java.util.Set;

@KafkaPayload
public record UrlsFilterRequest(Set<String> imageUrls, Set<String> thumbnailUrls) {
}
