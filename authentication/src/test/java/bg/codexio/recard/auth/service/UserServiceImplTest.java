package bg.codexio.recard.auth.service;

import bg.codexio.recard.auth.mapper.UserMapper;
import bg.codexio.recard.auth.model.entity.User;
import bg.codexio.recard.auth.exception.BadRequestException;
import bg.codexio.recard.auth.exception.NotFoundException;
import bg.codexio.recard.auth.model.payload.request.UserRegistrationRequest;
import bg.codexio.recard.auth.model.payload.response.UserAuthDetailsResponse;
import bg.codexio.recard.auth.model.payload.event.UserRegistrationEvent;
import bg.codexio.recard.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static UserRegistrationRequest userRegistrationRequest;
    private static User user;
    private static UserAuthDetailsResponse userAuthDetailsResponse;
    private static UserRegistrationEvent userRegistrationEventResponse;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private KafkaTemplate<String, UserRegistrationEvent> kafkaTemplate;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeAll
    static void setUp() {
        userRegistrationRequest = new UserRegistrationRequest("tester1", "Testing123@",
                "tets@abv", "Testing123@", "name", "name",
                LocalDate.of(1999, 4, 18));
        user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email");
        userAuthDetailsResponse = new UserAuthDetailsResponse("username", "email");
        userRegistrationEventResponse = new UserRegistrationEvent(1L, "name", "name",
                LocalDate.of(1999, 4, 18));
    }

    @Test
    void register_onNewUser_expectedSuccessfulRegistration() {
        final var topicName = "userregistrationrequest_topic";
        when(this.userMapper.map(any(User.class),
                any(UserRegistrationRequest.class)))
                .thenReturn(userRegistrationEventResponse);
        when(this.userRepository
                .getIfFree(anyString(), any(UserRegistrationRequest.class)))
                .thenReturn(Optional.of(userRegistrationRequest));
        when(this.userMapper.map(any(UserRegistrationRequest.class)))
                .thenReturn(user);
        when(this.userRepository.save(any(User.class))).thenReturn(user);
        when(this.kafkaTemplate
                .send(eq(topicName), any(UserRegistrationEvent.class)))
                .thenReturn(null);
        when(this.userMapper.map(any(User.class)))
                .thenReturn(userAuthDetailsResponse);

        this.userService.register(userRegistrationRequest);

        verify(this.userRepository, times(1)).getIfFree(anyString(),
                any(UserRegistrationRequest.class));
        verify(this.userMapper, times(1)).map(userRegistrationRequest);
        verify(this.userRepository, times(1)).save(user);
        verify(this.userMapper, times(1)).map(user);
        verify(this.kafkaTemplate, times(1)).send(eq(topicName),
                any(UserRegistrationEvent.class));
    }

    @Test
    void register_onExistingUser_expectedBadRequestException() {
        when(this.userRepository.getIfFree(userRegistrationRequest.username(),
                userRegistrationRequest))
                .thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> this.userService.register(userRegistrationRequest));
    }

    @Test
    public void getById_onExistingUser_expectedUserFound() {
        when(this.userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(this.userMapper.map(user)).thenReturn(userAuthDetailsResponse);

        final var actualResponse = this.userService.getById(user.getId());

        assertEquals(userAuthDetailsResponse, actualResponse);
        verify(this.userRepository).findById(user.getId());
        verify(this.userMapper).map(user);
    }

    @Test
    public void getById_onNotExistingUser_expectedUserNotFound() {
        when(this.userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> this.userService.getById(user.getId()));
        verify(this.userRepository).findById(user.getId());
    }
}