package bg.codexio.recard.profile.service;

import bg.codexio.recard.profile.mapper.ProfileMapper;
import bg.codexio.recard.profile.model.entity.Profile;
import bg.codexio.recard.profile.exception.ProfileNotFound;
import bg.codexio.recard.profile.model.payload.request.EditProfileRequest;
import bg.codexio.recard.profile.model.payload.response.ProfileResponse;
import bg.codexio.recard.profile.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {


    private static Profile profile;
    private static ProfileResponse profileResponse;
    private static EditProfileRequest editProfileRequest;
    private static ProfileResponse editProfileResponse;
    private static Profile modifiedProfile;

    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private ProfileMapper profileMapper;
    @InjectMocks
    private ProfileServiceImpl profileService;

    @BeforeAll
    static void setUp() {
        profile = new Profile();
        profile.setId(1L);
        profile.setFirstName("firstName");
        profile.setLastName("lastName");
        profile.setBornOn(LocalDate.of(1999, 4, 18));
        profileResponse = new ProfileResponse("firstName", "lastName",
                LocalDate.of(1999, 4, 18));
        editProfileRequest = new EditProfileRequest("John", "Doe",
                LocalDate.of(1950, 1, 18));
        editProfileResponse = new ProfileResponse("John", "Doe",
                LocalDate.of(1950, 1, 18));
        modifiedProfile = new Profile();
        modifiedProfile.setFirstName("John");
        modifiedProfile.setLastName("Doe");
        modifiedProfile.setBornOn(LocalDate.of(1950, 1, 18));
    }

    @Test
    void getById_onExistingProfile_expectedProfileFound() {
        when(this.profileRepository.findById(profile.getId())).thenReturn(Optional.of(profile));
        when(this.profileMapper.map(profile)).thenReturn(profileResponse);

        final var result = this.profileService.getByUserId(profile.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(profile.getFirstName(), result.firstName())
        );
        verify(this.profileRepository).findById(profile.getId());
        verify(this.profileMapper).map(profile);
    }

    @Test
    void getById_onNotExistingProfile_expectedProfileNotFound() {
        when(this.profileRepository.findById(profile.getId())).thenReturn(Optional.empty());

        assertThrows(ProfileNotFound.class, () -> this.profileService.getByUserId(profile.getId()));
        verify(this.profileRepository).findById(profile.getId());
    }

    @Test
    void edit_onExistingProfile_expectedProfileFound() {
        when(this.profileRepository.findById(profile.getId())).thenReturn(Optional.of(profile));
        when(this.profileMapper.map(editProfileRequest, profile)).thenReturn(modifiedProfile);
        when(this.profileRepository.save(modifiedProfile)).thenReturn(modifiedProfile);
        when(this.profileMapper.map(modifiedProfile)).thenReturn(editProfileResponse);

        final var result = this.profileService.edit(editProfileRequest, profile.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(modifiedProfile.getFirstName(), result.firstName())
        );
        verify(this.profileRepository).findById(profile.getId());
        verify(this.profileMapper).map(editProfileRequest, profile);
        verify(this.profileRepository).save(modifiedProfile);
        verify(this.profileMapper).map(modifiedProfile);
    }

    @Test
    void edit_onNotExistingProfile_expectedProfileNotFound() {
        when(this.profileRepository.findById(profile.getId())).thenReturn(Optional.empty());

        assertThrows(ProfileNotFound.class, () -> this.profileService.edit(editProfileRequest, profile.getId()));
        verify(this.profileRepository).findById(profile.getId());
    }
}