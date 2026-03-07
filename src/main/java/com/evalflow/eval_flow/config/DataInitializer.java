//package com.evalflow.eval_flow.config;
//
//import com.evalflow.eval_flow.model.Batch;
//import com.evalflow.eval_flow.model.Participant;
//import com.evalflow.eval_flow.model.Technology;
//import com.evalflow.eval_flow.model.User;
//import com.evalflow.eval_flow.repository.*;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.time.LocalDate;
//import java.util.Set;
//
//@Configuration
//public class DataInitializer {
//
//    @Bean
//    CommandLineRunner initDatabase(
//            BatchRepository batchRepo,
//            TechnologyRepository techRepo,
//            ParticipantRepository partRepo,
//            UserRepository userRepo,
//            RoleRepository roleRepo,
//            PasswordEncoder encoder) {
//
//        return args -> {
//            // 1. Create Roles
//            User.Role adminRole = new Role();
//            adminRole.setName("ROLE_ADMIN");
//            Role evalRole = new Role();
//            evalRole.setName("ROLE_EVALUATOR");
//            roleRepo.saveAll(Set.of(adminRole, evalRole));
//
//            // 2. Create Users (Ensuring NFR-2.1: Password Hashing)
//            User admin = new User();
//            admin.setUsername("admin");
//            admin.setPassword(encoder.encode("admin123"));
//            admin.setRoles(Set.of(adminRole));
//            userRepo.save(admin);
//
//            User eval = new User();
//            eval.setUsername("evaluator");
//            eval.setPassword(encoder.encode("eval123"));
//            eval.setRoles(Set.of(evalRole));
//            userRepo.save(eval);
//
//            // 3. Create Tech & Batch (FR-1.1, FR-1.2)
//            Technology java = new Technology(); java.setName("Java");
//            Technology cpp = new Technology(); cpp.setName("Cpp");
//            techRepo.saveAll(Set.of(java, cpp));
//
//            Batch b1 = new Batch();
//            b1.setName("Spring 2026 Developer Batch");
//            b1.setStartDate(LocalDate.now());
//            b1.setEndDate(LocalDate.now().plusMonths(3));
//            batchRepo.save(b1);
//
//            // 4. Create Participant (FR-3.1)
//            Participant p1 = new Participant();
//            p1.setName("Yash Kadav");
//            p1.setEmail("yashkadav52@gmail.com");
//            p1.setBatch(b1);
//            partRepo.save(p1);
//
//            System.out.println(">> Database Seeding for EvalFlow Completed Successfully.");
//        };
//    }
//}