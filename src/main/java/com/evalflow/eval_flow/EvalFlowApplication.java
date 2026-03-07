package com.evalflow.eval_flow;

import com.evalflow.eval_flow.model.Batch;
import com.evalflow.eval_flow.model.Technology;
import com.evalflow.eval_flow.model.User;
import com.evalflow.eval_flow.repository.BatchRepository;
import com.evalflow.eval_flow.repository.TechnologyRepository;
import com.evalflow.eval_flow.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;


@SpringBootApplication
public class EvalFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvalFlowApplication.class, args);

	}


	@Bean
	public CommandLineRunner loadData(
			UserRepository userRepository,
			BatchRepository batchRepository,
			TechnologyRepository technologyRepository,
			BCryptPasswordEncoder passwordEncoder) {
		return args -> {
			// Create default admin user
			if (userRepository.findByEmail("admin@example.com").isEmpty()) {
				User admin = new User();
				admin.setName("Admin User");
				admin.setEmail("admin@example.com");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setRole(User.Role.ADMIN);
				admin.setActive(true);
				userRepository.save(admin);
			}

			// Create default evaluator
			if (userRepository.findByEmail("evaluator@example.com").isEmpty()) {
				User evaluator = new User();
				evaluator.setName("John Evaluator");
				evaluator.setEmail("evaluator@example.com");
				evaluator.setPassword(passwordEncoder.encode("eval123"));
				evaluator.setRole(User.Role.EVALUATOR);
				evaluator.setActive(true);
				userRepository.save(evaluator);
			}

			// Create sample batch
			if (batchRepository.findByName("Batch 2024-01").isEmpty()) {
				Batch batch = new Batch();
				batch.setName("Batch 2024-01");
				batch.setStartDate(LocalDate.now());
				batch.setEndDate(LocalDate.now().plusMonths(3));
				batch.setActive(true);
				batchRepository.save(batch);
			}

			// Create sample technologies
			String[] techNames = {"Java", "Python", "React", "Angular", "Spring Boot"};
			for (String techName : techNames) {
				if (technologyRepository.findByName(techName).isEmpty()) {
					Technology tech = new Technology();
					tech.setName(techName);
					tech.setDescription("Technology: " + techName);
					tech.setActive(true);
					technologyRepository.save(tech);
				}
			}

			System.out.println("Sample data loaded successfully!");
			System.out.println("Admin login: admin@example.com / admin123");
			System.out.println("Evaluator login: evaluator@example.com / eval123");
		};
	}
}