package bg.codexio.recard.profile.controller;

import bg.codexio.recard.profile.constant.HeaderConstants;
import bg.codexio.recard.profile.model.payload.request.EditProfileRequest;
import bg.codexio.recard.profile.model.payload.response.ProfileResponse;
import bg.codexio.recard.profile.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponse edit(@Valid @RequestBody EditProfileRequest editProfileRequest,
                                @RequestHeader(HeaderConstants.USER_ID) Long userId) {
        return this.profileService.edit(editProfileRequest, userId);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponse get(@RequestHeader(HeaderConstants.USER_ID) Long userId) {
        return this.profileService.getByUserId(userId);
    }
}