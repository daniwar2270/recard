package bg.codexio.recard.profile.exception;

import bg.codexio.recard.profile.constant.MessageConstants;

public class ProfileNotFound extends RuntimeException {

    public ProfileNotFound() {
        super(MessageConstants.PROFILE_NOT_FOUND);
    }
}