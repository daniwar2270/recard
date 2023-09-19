package bg.codexio.recard.profile.service;

import bg.codexio.recard.profile.constant.KafkaConstants;
import bg.codexio.recard.profile.constant.TopicConstants;
import bg.codexio.recard.profile.mapper.ProfileMapper;
import bg.codexio.recard.profile.exception.ProfileNotFound;
import bg.codexio.recard.profile.model.payload.request.EditProfileRequest;
import bg.codexio.recard.profile.model.payload.response.ProfileResponse;
import bg.codexio.recard.profile.model.payload.event.ImageUploadEvent;
import bg.codexio.recard.profile.model.payload.event.UserRegistrationEvent;
import bg.codexio.recard.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @KafkaListener(topics = TopicConstants.TOPICS_USER_REGISTRATION,
            containerFactory = KafkaConstants.KAFKA_CONSUMER_FACTORY)
    private void register(UserRegistrationEvent userRegistrationEventResponse) {
        this.profileRepository.save(this.profileMapper.map(userRegistrationEventResponse));
    }

    @KafkaListener(topics = TopicConstants.TOPICS_USER_UPLOAD,
            containerFactory = KafkaConstants.KAFKA_CONSUMER_FACTORY)
    private void saveUrl(ImageUploadEvent imageUploadEvent) {
        this.profileRepository.findById(imageUploadEvent.userId())
                .map(profile -> this.profileMapper.map(imageUploadEvent, profile))
                .map(this.profileRepository::save)
                .orElseThrow(ProfileNotFound::new);
    }

    @Override
    public ProfileResponse edit(EditProfileRequest editProfileRequest, Long userId) {
        return this.profileRepository.findById(userId)
                .map(existingProfile -> this.profileMapper.map(editProfileRequest, existingProfile))
                .map(this.profileRepository::save)
                .map(this.profileMapper::map)
                .orElseThrow(ProfileNotFound::new);
    }

    @Override
    public ProfileResponse getByUserId(Long userId) {
        return this.profileRepository.findById(userId)
                .map(this.profileMapper::map)
                .orElseThrow(ProfileNotFound::new);
    }
}