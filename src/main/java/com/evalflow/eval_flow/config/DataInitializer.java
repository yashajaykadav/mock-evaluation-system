package com.evalflow.eval_flow.config;

import com.evalflow.eval_flow.model.*;
import com.evalflow.eval_flow.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(BatchRepository batchRepo, TechnologyRepository techRepo, ParticipantRepository partRepo){
        return args -> {
            Technology java = new Technology();
            java.setName("Java");
            Technology cpp = new Technology();
            cpp.setName("Cpp");
            techRepo.save(java);
            techRepo.save(cpp);

            Batch b1 = new Batch();
            b1.setName("Spring 2026 Developer Batch");
            b1.setStartDate(LocalDate.now());
            b1.setEndDate(LocalDate.now().plusMonths(3));
            batchRepo.save(b1);

            Participant p1 = new Participant();
            p1.setName("Yash Kadav");
            p1.setEmail("yashkadav52@gmail.com");
            p1.setBatch(b1);
            partRepo.save(p1);
            System.out.println(">>Seeded Data Loaded Successfully");

        };
    }
}
