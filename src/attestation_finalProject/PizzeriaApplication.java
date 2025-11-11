package attestation_finalProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "attestation_finalProject")
@EntityScan(basePackages = "attestation_finalProject.entity")
@EnableJpaRepositories(basePackages = "attestation_finalProject.repository")
public class PizzeriaApplication {
    public static void main(String[] args) {
        SpringApplication.run(PizzeriaApplication.class, args);
    }
}