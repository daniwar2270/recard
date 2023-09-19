package bg.codexio.recard.auth.service;

import bg.codexio.recard.auth.model.payload.request.UserRegistrationRequest;
import bg.codexio.recard.auth.model.payload.response.UserAuthDetailsResponse;

public interface UserService {

    UserAuthDetailsResponse register(UserRegistrationRequest userRegistrationRequest);

    UserAuthDetailsResponse getById(Long id);
}