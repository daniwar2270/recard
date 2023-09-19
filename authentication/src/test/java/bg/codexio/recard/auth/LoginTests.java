package bg.codexio.recard.auth;

import bg.codexio.recard.auth.model.entity.User;
import bg.codexio.recard.auth.exception.UnauthorizedException;
import bg.codexio.recard.auth.model.payload.request.AuthenticationRequest;
import bg.codexio.recard.auth.repository.UserRepository;
import bg.codexio.recard.auth.service.AuthService;
import bg.codexio.recard.auth.service.JwtService;
import bg.codexio.recard.auth.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginTests {

    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthService authService;
    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    private final String username = "testuser";
    private final String password = "testpassword";

    @Test
    public void login_onValidCredentials_expectedAuthenticationResponse() {
        final var encodedPassword = "encodedpassword";
        final var accessToken = "access_token";
        final var refreshToken = "refresh_token";

        final var request = new AuthenticationRequest(this.username, this.password);
        final var user = new User();
        user.setId(1L);
        user.setPassword(encodedPassword);

        final var userDetails = mock(UserDetails.class);
        when(this.userRepository.findByUsername(this.username)).thenReturn(Optional.of(user));
        when(this.userDetailsService.loadUserByUsername(this.username)).thenReturn(userDetails);
        when(this.passwordEncoder.matches(this.password, encodedPassword)).thenReturn(true);
        when(this.jwtService.generateToken(userDetails, user.getId())).thenReturn(accessToken);
        when(this.jwtService.generateRefreshToken(userDetails, user.getId())).thenReturn(refreshToken);
        final var response = this.authService.login(request);

        assertNotNull(response);
        assertEquals(accessToken, response.accessToken());
        assertEquals(refreshToken, response.refreshToken());
        verify(this.userRepository).findByUsername(this.username);
        verify(this.userDetailsService).loadUserByUsername(this.username);
        verify(this.passwordEncoder).matches(this.stringArgumentCaptor.capture(), this.stringArgumentCaptor.capture());
        assertEquals(this.password, this.stringArgumentCaptor.getAllValues().get(0)); // Verify password passed to matches()
        assertEquals(encodedPassword, this.stringArgumentCaptor.getAllValues().get(1)); // Verify encoded password passed to matches()
        verify(this.jwtService).generateToken(userDetails, user.getId());
        verify(this.jwtService).generateRefreshToken(userDetails, user.getId());
    }

    @Test
    public void login_onInvalidCredentials_expectedUnauthorizedException() {
        final var request = new AuthenticationRequest(this.username, this.password);
        when(this.userRepository.findByUsername(this.username)).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> this.authService.login(request));
        verify(this.userRepository).findByUsername(this.username);
        verifyNoInteractions(this.userDetailsService);
        verifyNoInteractions(this.passwordEncoder);
        verifyNoInteractions(this.jwtService);
    }
}