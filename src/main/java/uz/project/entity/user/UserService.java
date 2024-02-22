package uz.project.entity.user;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uz.project.common.constant.Status;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.request.PageableRequestUtil;
import uz.project.common.request.SearchSpecification;
import uz.project.common.response.status.UserStatus;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findById(Long id) {
        if (id == null) return Optional.empty();
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<User> getList(PageableRequest pageable) {
        return userRepository.findAll(new SearchSpecification<>(pageable.getSearch()), PageableRequestUtil.toPageable(pageable));
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return findById(id).orElseThrow(() -> new ApiException(UserStatus.NOT_FOUND));
    }

    @Transactional
    public User save(User user, UserPayload payload) {
        user.setFirstName(payload.getFirstName());
        user.setLastName(payload.getLastName());
        user.setUsername(payload.getUsername());
        if (StringUtils.hasText(payload.getPassword())) {
            user.setPassword(passwordEncoder.encode(payload.getPassword()));
        }
        user.setRole(payload.getRole());
        user.setStatus(payload.getStatus());
        return userRepository.saveAndFlush(user);
    }

    public User create(UserPayload payload) {
        if (userRepository.existsByUsername(payload.getUsername()))
            throw new ApiException(UserStatus.USERNAME_ALREADY_EXIST);
        User user = new User();
        return save(user, payload);
    }

    public User update(Long id, UserPayload payload) {
        if (userRepository.existsByIdNotAndUsername(id, payload.getUsername()))
            throw new ApiException(UserStatus.USERNAME_ALREADY_EXIST);
        User user = getById(id);
        return save(user, payload);
    }

    public void delete(Long id) {
        User user = getById(id);
        user.setStatus(Status.DELETED);
        userRepository.saveAndFlush(user);
    }
}
