package kz.app.umbrella.api.controller;

import kz.app.umbrella.api.service.UserService;
import kz.app.umbrella.api.utils.JwtUtils;
import kz.app.umbrella.dto.AuthResponse;
import kz.app.umbrella.dto.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {
        userService.save(authRequest);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password())
        );
        String jwt = jwtUtils.generateJwtToken(authentication);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}
