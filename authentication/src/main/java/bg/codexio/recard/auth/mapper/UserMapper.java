package bg.codexio.recard.auth.mapper;

import bg.codexio.recard.auth.config.CustomPasswordEncoder;
import bg.codexio.recard.auth.model.payload.request.UserRegistrationRequest;
import bg.codexio.recard.auth.model.payload.response.UserAuthDetailsResponse;
import bg.codexio.recard.auth.model.payload.event.UserRegistrationEvent;
import bg.codexio.recard.auth.model.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = CustomPasswordEncoder.class)
public interface UserMapper {

    @Mapping(target = "password", qualifiedByName = "encode")
    User map(UserRegistrationRequest userRegistrationRequest);

    UserAuthDetailsResponse map(User user);

    UserRegistrationEvent map(User user, UserRegistrationRequest userRegistrationRequest);
}