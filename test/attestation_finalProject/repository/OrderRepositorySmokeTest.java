package attestation_finalProject.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/** Простейший smoke: бин OrderRepository создаётся в JPA-срезе. */
@DataJpaTest
class OrderRepositorySmokeTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("OrderRepository создаётся (smoke)")
    void repositoryBean_isCreated() {
        assertThat(orderRepository).isNotNull();
    }
}
