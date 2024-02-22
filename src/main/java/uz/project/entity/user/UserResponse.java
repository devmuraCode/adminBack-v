package uz.project.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.Role;
import uz.project.common.response.TechnicalFieldsResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse extends TechnicalFieldsResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String username;
    private Role role;

    public UserResponse(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.fullName = getFullName(user);
        this.username = user.getUsername();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.status = user.getStatus();
    }

    public static UserResponse minResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFullName(getFullName(user));
        return userResponse;
    }

    public static String getFullName(User user) {
        return String.format("%s %s", user.getFirstName(), user.getLastName());
    }
}
