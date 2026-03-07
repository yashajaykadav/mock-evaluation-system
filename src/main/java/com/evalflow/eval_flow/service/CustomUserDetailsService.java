//package com.evalflow.eval_flow.service;
//
//import com.evalflow.eval_flow.model.User;
//import com.evalflow.eval_flow.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        User user = userRepository.findByusername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//
//        return org.springframework.security.core.userdetails.User
//                .withUsername(user.getUsername())
//                .password(user.getPassword())
//                .authorities(
//                        user.getRoles().stream()
//                                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                                .toList()
//                )
//                .build();
//    }
//}