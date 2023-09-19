package bg.codexio.recard.auth.service;

import bg.codexio.recard.auth.constants.MessageConstants;
import bg.codexio.recard.auth.mapper.UserMapper;
import bg.codexio.recard.auth.model.entity.User;
import bg.codexio.recard.auth.exception.BadRequestException;
import bg.codexio.recard.auth.exception.NotFoundException;
import bg.codexio.recard.auth.model.payload.event.UserRegistrationEvent;
import bg.codexio.recard.auth.model.payload.request.UserRegistrationRequest;
import bg.codexio.recard.auth.model.payload.response.UserAuthDetailsResponse;
import bg.codexio.recard.auth.model.payload.event.UserIdEvent;
import bg.codexio.recard.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public UserAuthDetailsResponse register(UserRegistrationRequest userRegistrationRequest) {
        return this.userRepository
                .getIfFree(userRegistrationRequest.username(), userRegistrationRequest)
                .map(this.userMapper::map)
                .map(this.userRepository::save)
                .map(saved -> {
                    final var response = this.userMapper.map(saved);
                    this.publishUserDetails(saved, userRegistrationRequest);
                    this.publishUserId(saved.getId());
                    return response;
                })
                .orElseThrow(() -> new BadRequestException(MessageConstants.USER_EXISTS));
    }

    @Override
    public UserAuthDetailsResponse getById(Long id) {
        return this.userRepository.findById(id)
                .map(this.userMapper::map)
                .orElseThrow(() -> new NotFoundException(MessageConstants.USER_NOT_FOUND));
    }

    private void publishUserDetails(User user, UserRegistrationRequest userRegistrationRequest) {
        this.kafkaTemplate.send(
                TopicNamingService.getTopicName(UserRegistrationEvent.class),
                this.userMapper.map(user, userRegistrationRequest)
        );
    }

    private void publishUserId(Long id) {
        this.kafkaTemplate
                .send(TopicNamingService.getTopicName(UserIdEvent.class), new UserIdEvent(id));
    }
}