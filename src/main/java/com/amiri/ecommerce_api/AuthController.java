package com.amiri.ecommerce_api;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    record SignUpRequest(String name, String email, String password) {}
    record LoginRequest(String email, String password) {}
    record UserResponse(String id, String name, String email) {}
    record AuthResponse(String token, UserResponse user) {}

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthController(AppUserRepository userRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse signUp(@RequestBody SignUpRequest request) {
        if (request.name() == null || request.name().isBlank()
                || request.email() == null || request.email().isBlank()
                || request.password() == null || request.password().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sign up data");
        }

        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        String hash = passwordEncoder.encode(request.password());
        AppUser user = userRepository.save(new AppUser(request.name().trim(), email, hash));

        return toAuthResponse(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        if (request.email() == null || request.password() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password");
        }

        AppUser user = userRepository.findByEmail(request.email().trim().toLowerCase())
                .filter(u -> passwordEncoder.matches(request.password(), u.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password"));

        return toAuthResponse(user);
    }

    private AuthResponse toAuthResponse(AppUser user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getId())
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        return new AuthResponse(token, new UserResponse(user.getId(), user.getName(), user.getEmail()));
    }
}
