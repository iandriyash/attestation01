package attestation.attestation03.repository;

import attestation.attestation03.entity.Order;
import attestation.attestation03.entity.OrderItem;
import attestation.attestation03.entity.Pizza;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void findAllActive_ShouldReturnOnlyActiveOrders() {
        Pizza pizza = new Pizza();
        pizza.setName("Маргарита");
        pizza.setDescription("Классика");
        pizza.setPrice(BigDecimal.valueOf(450));
        pizza.setIsDeleted(false);
        entityManager.persist(pizza);

        Order activeOrder = new Order();
        activeOrder.setCustomerName("Иван");
        activeOrder.setCustomerPhone("+79991234567");
        activeOrder.setTotalPrice(BigDecimal.valueOf(900));
        activeOrder.setStatus("NEW");
        activeOrder.setIsDeleted(false);
        entityManager.persist(activeOrder);

        Order deletedOrder = new Order();
        deletedOrder.setCustomerName("Петр");
        deletedOrder.setCustomerPhone("+79991234568");
        deletedOrder.setTotalPrice(BigDecimal.valueOf(500));
        deletedOrder.setStatus("CANCELLED");
        deletedOrder.setIsDeleted(true);
        entityManager.persist(deletedOrder);

        entityManager.flush();

        List<Order> result = orderRepository.findAllActive();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCustomerName()).isEqualTo("Иван");
    }

    @Test
    void findByIdActive_WhenExists_ShouldReturnOrder() {
        Order order = new Order();
        order.setCustomerName("Алексей");
        order.setCustomerPhone("+79991234569");
        order.setTotalPrice(BigDecimal.valueOf(1200));
        order.setStatus("NEW");
        order.setIsDeleted(false);
        entityManager.persist(order);
        entityManager.flush();

        Optional<Order> result = orderRepository.findByIdActive(order.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getCustomerName()).isEqualTo("Алексей");
    }

    @Test
    void findByIdActive_WhenDeleted_ShouldReturnEmpty() {
        Order order = new Order();
        order.setCustomerName("Удалённый");
        order.setCustomerPhone("+79991234570");
        order.setTotalPrice(BigDecimal.valueOf(500));
        order.setStatus("CANCELLED");
        order.setIsDeleted(true);
        entityManager.persist(order);
        entityManager.flush();

        Optional<Order> result = orderRepository.findByIdActive(order.getId());

        assertThat(result).isEmpty();
    }

    @Test
    void findByStatusAndNotDeleted_ShouldReturnMatchingOrders() {
        Order order1 = new Order();
        order1.setCustomerName("Иван");
        order1.setCustomerPhone("+79991234571");
        order1.setTotalPrice(BigDecimal.valueOf(900));
        order1.setStatus("NEW");
        order1.setIsDeleted(false);
        entityManager.persist(order1);

        Order order2 = new Order();
        order2.setCustomerName("Петр");
        order2.setCustomerPhone("+79991234572");
        order2.setTotalPrice(BigDecimal.valueOf(1200));
        order2.setStatus("COMPLETED");
        order2.setIsDeleted(false);
        entityManager.persist(order2);

        entityManager.flush();

        List<Order> result = orderRepository.findByStatusAndNotDeleted("NEW");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCustomerName()).isEqualTo("Иван");
    }

    @Test
    void findByCustomerPhoneAndNotDeleted_ShouldReturnMatchingOrders() {
        Order order1 = new Order();
        order1.setCustomerName("Иван");
        order1.setCustomerPhone("+79991234573");
        order1.setTotalPrice(BigDecimal.valueOf(900));
        order1.setStatus("NEW");
        order1.setIsDeleted(false);
        entityManager.persist(order1);

        Order order2 = new Order();
        order2.setCustomerName("Иван");
        order2.setCustomerPhone("+79991234573");
        order2.setTotalPrice(BigDecimal.valueOf(1200));
        order2.setStatus("COMPLETED");
        order2.setIsDeleted(false);
        entityManager.persist(order2);

        Order order3 = new Order();
        order3.setCustomerName("Петр");
        order3.setCustomerPhone("+79991234574");
        order3.setTotalPrice(BigDecimal.valueOf(500));
        order3.setStatus("NEW");
        order3.setIsDeleted(false);
        entityManager.persist(order3);

        entityManager.flush();

        List<Order> result = orderRepository.findByCustomerPhoneAndNotDeleted("+79991234573");

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(order -> order.getCustomerPhone().equals("+79991234573"));
    }
}