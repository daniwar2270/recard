package bg.codexio.recard.profile.service;

import bg.codexio.recard.profile.model.payload.request.EditProfileRequest;
import bg.codexio.recard.profile.model.payload.response.ProfileResponse;

public interface ProfileService {

    ProfileResponse edit(EditProfileRequest editProfileRequest, Long userId);

    ProfileResponse getByUserId(Long userId);
}