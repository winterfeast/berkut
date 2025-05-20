package kz.app.umbrella.api.controller;

import kz.app.umbrella.api.dto.MessageRequest;
import kz.app.umbrella.api.dto.MessageResponse;
import kz.app.umbrella.api.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<Void> sendMessage(@RequestBody MessageRequest request, Principal principal) {
        messageService.sendMessage(principal.getName(), request.text());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MessageResponse>> getMessages(Principal principal) {
        return ResponseEntity.ok(messageService.getUserMessages(principal.getName()));
    }
}
