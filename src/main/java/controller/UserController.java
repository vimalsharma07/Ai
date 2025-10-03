package controller;

import java.util.List;
import java.util.Map; // <-- Add this
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import model.User;
import repository.UserRepository;
import service.UserService;
import service.JwtService;
import service.AIService;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final AIService aiService;

    @Autowired
    private EntityManager entityManager;

    // Constructor with AIService included
    public UserController(UserRepository userRepository, UserService userService, JwtService jwtService, AIService aiService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtService = jwtService;
        this.aiService = aiService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public static class RegisterUserRequest {
        @NotBlank @Size(min = 3, max = 50)
        public String username;

        @NotBlank @Email @Size(max = 100)
        public String email;

        @NotBlank @Size(min = 6, max = 100)
        public String password;
    }

    public record RegisterResponse(Long id, String username, String email) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserRequest body) {
        User created = userService.register(body.username, body.email, body.password);
        return ResponseEntity.ok(new RegisterResponse(created.getId(), created.getUsername(), created.getEmail()));
    }

    public static class LoginRequest {
        @NotBlank
        public String usernameOrEmail;

        @NotBlank
        public String password;
    }

    public record LoginResponse(String name, String email, String token) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest body) {
        User user = userService.authenticate(body.usernameOrEmail, body.password);
        String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getEmail());
        return ResponseEntity.ok(new LoginResponse(user.getUsername(), user.getEmail(), token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Debug endpoints...
    @GetMapping("/debug/tables")
    public String checkTables() {
        try {
            Query query = entityManager.createNativeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'");
            List<Object> tables = query.getResultList();
            return "Tables found: " + tables.toString();
        } catch (Exception e) {
            return "Error checking tables: " + e.getMessage();
        }
    }

    @GetMapping("/debug/roles")
    public String checkRoles() {
        try {
            Query query = entityManager.createNativeQuery("SELECT * FROM roles");
            List<Object[]> roles = query.getResultList();
            StringBuilder result = new StringBuilder("Roles found:\n");
            for (Object[] role : roles) {
                result.append("ID: ").append(role[0]).append(", Name: ").append(role[1]).append("\n");
            }
            return result.toString();
        } catch (Exception e) {
            return "Error checking roles: " + e.getMessage();
        }
    }

    @GetMapping("/debug/users")
    public String checkUsers() {
        try {
            Query query = entityManager.createNativeQuery("SELECT * FROM users");
            List<Object[]> users = query.getResultList();
            StringBuilder result = new StringBuilder("Users found:\n");
            for (Object[] user : users) {
                result.append("ID: ").append(user[0]).append(", Username: ").append(user[2]).append(", Email: ").append(user[3]).append("\n");
            }
            return result.toString();
        } catch (Exception e) {
            return "Error checking users: " + e.getMessage();
        }
    }

    // AI POST endpoint
    @PostMapping("/ai")
    public ResponseEntity<?> AiChat(
            @RequestHeader(name = "Authorization", required = false) String authHeader,
            @RequestBody Map<String, String> request) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        boolean valid = jwtService.validateToken(token);

        if (!valid) {
            return ResponseEntity.status(401).body("Invalid or expired JWT token");
        }

        String prompt = request.get("prompt");
        if (prompt == null || prompt.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing 'prompt' field in request");
        }

        String aiResponse = aiService.getAIResponse(prompt);
        String finalResponse = aiResponse.replace("\\n", "\n").replace("\\\"", "\"");
        return ResponseEntity.ok(Map.of("response", finalResponse));
    }
}
