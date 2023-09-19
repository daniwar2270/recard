package bg.codexio.recard.profile.model.payload.request;

import bg.codexio.recard.profile.constant.MessageConstants;
import bg.codexio.recard.profile.constant.RegexConstants;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EditProfileRequest(

        @Pattern(regexp = RegexConstants.NAME_REGEX, message = MessageConstants.INVALID_FORMAT_FOR_FIRST_NAME)
        String firstName,

        @Pattern(regexp = RegexConstants.NAME_REGEX, message = MessageConstants.INVALID_FORMAT_FOR_LAST_NAME)
        String lastName,

        @Past(message = MessageConstants.INVALID_DATE)
        LocalDate bornOn) {
}