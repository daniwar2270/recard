package bg.codexio.recard.auth.controller;

import bg.codexio.recard.auth.constants.HeaderConstants;
import bg.codexio.recard.auth.model.payload.request.UserRegistrationRequest;
import bg.codexio.recard.auth.model.payload.response.UserAuthDetailsResponse;
import bg.codexio.recard.auth.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserAuthDetailsResponse register(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        return this.userService.register(userRegistrationRequest);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserAuthDetailsResponse getById(@RequestHeader(HeaderConstants.USER_ID) Long id) {
        return this.userService.getById(id);
    }
}