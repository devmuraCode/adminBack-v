package uz.project.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.project.common.constant.Event.OnCreate;
import uz.project.common.constant.Event.OnUpdate;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.response.SuccessDataIterable;
import uz.project.common.response.SuccessfulResponse;
import uz.project.common.response.status.UserStatus;
import uz.project.entity.user.UserPayload;
import uz.project.entity.user.UserResponse;
import uz.project.entity.user.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/users")
public class UserAdminController {

    private final UserService userService;

    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

//    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping("/pageable")
    public SuccessDataIterable<UserResponse> getList(@Valid @RequestBody PageableRequest pageable) {
        Page<UserResponse> response = userService.getList(pageable).map(UserResponse::new);
        return new SuccessDataIterable<>(response);
    }

//    @PreAuthorize(value = "hasRole('ADMIN')")
    @GetMapping("/{id}")
    public SuccessfulResponse<UserResponse> getById(@PathVariable Long id) {
        UserResponse response = new UserResponse(userService.getById(id));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping
    public SuccessfulResponse<UserResponse> create(@Validated(value = OnCreate.class) @RequestBody UserPayload payload) {
        UserResponse response = new UserResponse(userService.create(payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasRole('ADMIN')")
    @PutMapping("/{id}")
    public SuccessfulResponse<UserResponse> update(@PathVariable Long id, @Validated(value = OnUpdate.class) @RequestBody UserPayload payload) {
        UserResponse response = new UserResponse(userService.update(id, payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public SuccessfulResponse<?> delete(@PathVariable Long id) {
        userService.delete(id);
        throw new ApiException(UserStatus.DELETED);
    }
}
