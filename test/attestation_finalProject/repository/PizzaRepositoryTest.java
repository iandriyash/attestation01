package attestation_finalProject.repository;

import attestation_finalProject.entity.Pizza;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PizzaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private attestation_finalProject.repository.PizzaRepository pizzaRepository;

    @Test
    void findAllActive_ShouldReturnOnlyActivePizzas() {
        Pizza activePizza = new Pizza();
        activePizza.setName("Тестовая Активная Пицца");
        activePizza.setDescription("Описание");
        activePizza.setPrice(BigDecimal.valueOf(450));
        activePizza.setIsDeleted(false);
        entityManager.persist(activePizza);

        Pizza deletedPizza = new Pizza();
        deletedPizza.setName("Тестовая Удалённая");
        deletedPizza.setDescription("Удалена");
        deletedPizza.setPrice(BigDecimal.valueOf(500));
        deletedPizza.setIsDeleted(true);
        entityManager.persist(deletedPizza);

        entityManager.flush();

        List<Pizza> result = pizzaRepository.findAllActive();

        assertThat(result).anyMatch(p -> p.getName().equals("Тестовая Активная Пицца"));
        assertThat(result).noneMatch(p -> p.getName().equals("Тестовая Удалённая"));
    }

    @Test
    void findByIdActive_WhenExists_ShouldReturnPizza() {
        Pizza pizza = new Pizza();
        pizza.setName("Пепперони Тестовая");
        pizza.setDescription("Острая");
        pizza.setPrice(BigDecimal.valueOf(550));
        pizza.setIsDeleted(false);
        entityManager.persist(pizza);
        entityManager.flush();

        Optional<Pizza> result = pizzaRepository.findByIdActive(pizza.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Пепперони Тестовая");
    }

    @Test
    void findByIdActive_WhenDeleted_ShouldReturnEmpty() {
        Pizza pizza = new Pizza();
        pizza.setName("Удалённая Тестовая");
        pizza.setDescription("Удалена");
        pizza.setPrice(BigDecimal.valueOf(500));
        pizza.setIsDeleted(true);
        entityManager.persist(pizza);
        entityManager.flush();

        Optional<Pizza> result = pizzaRepository.findByIdActive(pizza.getId());

        assertThat(result).isEmpty();
    }

    @Test
    void findByNameContainingIgnoreCaseAndNotDeleted_ShouldReturnMatchingPizzas() {
        Pizza pizza1 = new Pizza();
        pizza1.setName("Супермаргарита Тестовая");
        pizza1.setDescription("Классическая");
        pizza1.setPrice(BigDecimal.valueOf(450));
        pizza1.setIsDeleted(false);
        entityManager.persist(pizza1);

        Pizza pizza2 = new Pizza();
        pizza2.setName("Пепперони Тестовая Делюкс");
        pizza2.setDescription("Острая");
        pizza2.setPrice(BigDecimal.valueOf(550));
        pizza2.setIsDeleted(false);
        entityManager.persist(pizza2);

        entityManager.flush();

        List<Pizza> result = pizzaRepository.findByNameContainingIgnoreCaseAndNotDeleted("супермарга");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Супермаргарита Тестовая");
    }
}