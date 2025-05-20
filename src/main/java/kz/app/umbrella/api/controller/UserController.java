package kz.app.umbrella.api.controller;

import kz.app.umbrella.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/telegram-token")
    public ResponseEntity<String> generateTelegramToken(Principal principal) {
        String token = userService.generateTelegramTokenForUser(principal.getName());
        return ResponseEntity.ok(token);
    }
}
