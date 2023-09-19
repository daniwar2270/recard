package bg.codexio.recard.profile.scheduler;

import bg.codexio.recard.profile.model.payload.request.UrlsFilterRequest;
import bg.codexio.recard.profile.repository.ProfileRepository;
import bg.codexio.recard.profile.service.TopicNamingService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class ScheduledUrlSender {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ProfileRepository profileRepository;

    @Scheduled(cron = "${url.scheduler.filter.time}")
    public void send() {
        final var imageUrls = this.profileRepository.findAllImageUrls();
        final var thumbNailUrls = this.profileRepository.findAllThumbnailUrls();

        this.publish(imageUrls, thumbNailUrls);
    }

    private void publish(Set<String> images, Set<String> thumbNails) {
        final var filterUrlsRequest = new UrlsFilterRequest(images, thumbNails);
        this.kafkaTemplate.send(TopicNamingService.getTopicName(UrlsFilterRequest.class), filterUrlsRequest);
    }
}
