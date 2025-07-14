package gr.aueb.cf.insuranceapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InsuranceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(InsuranceAppApplication.class, args);
	}

}
