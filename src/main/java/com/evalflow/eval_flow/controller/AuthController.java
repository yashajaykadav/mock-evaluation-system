package com.evalflow.eval_flow.controller;

import com.evalflow.eval_flow.entity.User;
import com.evalflow.eval_flow.repository.UserRepository;
import com.evalflow.eval_flow.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String password = body.get("password");
            
            String token = authService.login(email, password);
            User user = userRepository.findByEmail(email).orElseThrow();
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "token", token,
                    "role", user.getRole().name()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}