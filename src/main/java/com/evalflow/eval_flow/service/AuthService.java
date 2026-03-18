package com.evalflow.eval_flow.service;

import com.evalflow.eval_flow.entity.Role;
import com.evalflow.eval_flow.entity.User;
import com.evalflow.eval_flow.repository.UserRepository;
import com.evalflow.eval_flow.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.EVALUATOR); // default role

        userRepository.save(user);

        return jwtService.generateToken(user.getEmail());
    }

    public String login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtService.generateToken(email);
    }
}