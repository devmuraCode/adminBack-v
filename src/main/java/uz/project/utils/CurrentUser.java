package uz.project.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.project.common.exception.AccessDeniedException;
import uz.project.entity.user.User;

import java.util.Optional;

@UtilityClass
public class CurrentUser {

    public static User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            return (User) authentication.getPrincipal();
        } catch (Exception e) {
            throw new AccessDeniedException("Token not valid");
        }
    }

    public static Optional<Long> getCurrentUserId() {
        try {
            return Optional.ofNullable(getCurrentUser()).map(User::getId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
