package lt.vu.bakalauras;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages={"lt.vu.bakalauras.repository"})
public class BakalaurasApplication {

    public static void main(String[] args) {
        SpringApplication.run(BakalaurasApplication.class, args);
    }

}
