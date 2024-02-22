package uz.project.controller;

import org.springframework.web.bind.annotation.*;
import uz.project.common.response.SuccessfulResponse;
import uz.project.entity.auth.AuthService;
import uz.project.entity.auth.LoginPayload;
import uz.project.entity.auth.TokenResponse;
import uz.project.entity.user.UserResponse;
import uz.project.utils.CurrentUser;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public SuccessfulResponse<TokenResponse> login(@Valid @RequestBody LoginPayload payload) {
        TokenResponse response = new TokenResponse(authService.login(payload));
        return new SuccessfulResponse<>(response);
    }

    @GetMapping("/me")
    public SuccessfulResponse<UserResponse> getMe() {
        UserResponse response = new UserResponse(CurrentUser.getCurrentUser());
        return new SuccessfulResponse<>(response);
    }
}
