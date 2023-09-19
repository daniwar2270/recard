package bg.codexio.recard.profile.mapper;

import bg.codexio.recard.profile.model.entity.Profile;
import bg.codexio.recard.profile.model.payload.request.EditProfileRequest;
import bg.codexio.recard.profile.model.payload.event.UserRegistrationEvent;
import bg.codexio.recard.profile.model.payload.response.ProfileResponse;
import bg.codexio.recard.profile.model.payload.event.ImageUploadEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfileMapper {

    Profile map(EditProfileRequest editProfileRequest, @MappingTarget Profile profile);

    Profile map(ImageUploadEvent userUploadImageRequest, @MappingTarget Profile profile);

    ProfileResponse map(Profile profile);

    Profile map(UserRegistrationEvent request);
}