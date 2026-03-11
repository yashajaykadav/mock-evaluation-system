//package com.evalflow.eval_flow.controller;
//
//import com.evalflow.eval_flow.model.User;
//import com.evalflow.eval_flow.service.UserService;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@Controller
//public class AuthController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/")
//    public String home() {
//        return "redirect:/login";
//    }
//
//    @GetMapping("/login")
//    public String loginPage() {
//        return "login";
//    }
//
//    @GetMapping("/dashboard")
//    public String dashboard(HttpSession session, Model model) {
//        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            return "redirect:/login";
//        }
//
//        if (user.getRole() == User.Role.ADMIN) {
//            return "redirect:/admin/dashboard";
//        } else {
//            return "redirect:/evaluator/dashboard";
//        }
//    }
//
//    @PostMapping("/api/login")
//    @ResponseBody
//    public Map<String, Object> login(@RequestBody Map<String, String> credentials, HttpSession session) {
//        Map<String, Object> response = new HashMap<>();
//
//        Optional<User> userOpt = userService.getUserByEmail(credentials.get("email"));
//
//        if (userOpt.isPresent()) {
//            User user = userOpt.get();
//            if (userService.validatePassword(credentials.get("password"), user.getPassword())) {
//                session.setAttribute("user", user);
//                response.put("success", true);
//                response.put("role", user.getRole().toString());
//                return response;
//            }
//        }
//
//        response.put("success", false);
//        response.put("message", "Invalid credentials");
//        return response;
//    }
//
//    @PostMapping("/api/logout")
//    @ResponseBody
//    public Map<String, Object> logout(HttpSession session) {
//        session.invalidate();
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        return response;
//    }
//}