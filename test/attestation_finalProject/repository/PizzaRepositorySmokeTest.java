package attestation_finalProject.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Простейший smoke-тест репозитория.
 * Поднимает JPA-срез и проверяет, что бин PizzaRepository доступен.
 */
@DataJpaTest
class PizzaRepositorySmokeTest {

    @Autowired
    private PizzaRepository pizzaRepository;

    @Test
    @DisplayName("PizzaRepository создаётся (smoke)")
    void repositoryBean_isCreated() {
        assertThat(pizzaRepository).isNotNull();
    }
}
