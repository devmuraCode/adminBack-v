package uz.project.entity.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import uz.project.common.exception.ApiException;
import uz.project.common.response.status.UserStatus;
import uz.project.entity.user.User;
import uz.project.security.JwtProvider;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public AuthService(JwtProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    public String login(LoginPayload payload) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    payload.getUsername(),
                                    payload.getPassword()
                            )
                    );
            User user = (User) authenticate.getPrincipal();
            return jwtProvider.generateToken(payload.getUsername(), user.getRole());
        } catch (BadCredentialsException e) {
            throw new ApiException(UserStatus.USERNAME_OR_PASSWORD_INCORRECT);
        }
    }
}
