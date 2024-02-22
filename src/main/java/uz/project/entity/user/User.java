package uz.project.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.project.common.TechnicalFields;
import uz.project.common.constant.Role;
import uz.project.common.constant.Status;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends TechnicalFields implements UserDetails {

    @Id
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired = true;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired = true;

    public User(String firstName, String lastName, String username, String password, Role role, Status status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.role = role;
        super.setStatus(status);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<Permission> permissions = this.role.getPermissions();
//
//        Set<GrantedAuthority> authorities = new HashSet<>();
//
//        for (Permission permission : permissions) {
//            authorities.add(new SimpleGrantedAuthority(permission.name()));
//        }

//        return authorities;
        return null;
    }

    @Override
    public boolean isEnabled() {
        return Status.ACTIVE.equals(getStatus());
    }
}
