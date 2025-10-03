package service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import model.User;
import repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(String username, String email, String rawPassword) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            throw new IllegalArgumentException("Username already in use");
        }
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoleId(null);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User authenticate(String usernameOrEmail, String rawPassword) {
        Optional<User> byUsername = userRepository.findByUsername(usernameOrEmail);
        Optional<User> byEmail = byUsername.isPresent() ? byUsername : userRepository.findByEmail(usernameOrEmail);
        User user = byEmail.orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return user;
    }
}


