package uz.project.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.Event.OnCreate;
import uz.project.common.constant.Event.OnUpdate;
import uz.project.common.constant.Role;
import uz.project.common.constant.Status;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPayload {

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String firstName;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String lastName;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String username;
    @NotBlank(groups = OnCreate.class)
    private String password;
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private Role role;
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private Status status;
}
