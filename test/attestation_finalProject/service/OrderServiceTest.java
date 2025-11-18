package attestation_finalProject.service;

import attestation_finalProject.dto.OrderDto;
import attestation_finalProject.entity.Order;
import attestation_finalProject.repository.OrderRepository;
import attestation_finalProject.repository.OrderItemRepository;
import attestation_finalProject.repository.PizzaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit-тесты для OrderService.
 * Используем чистый Mockito без Spring-контекста.
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private PizzaRepository pizzaRepository;

    @InjectMocks
    private OrderService orderService;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setCustomerName("Иван Иванов");
        testOrder.setCustomerPhone("+79991234567");
        testOrder.setTotalPrice(BigDecimal.valueOf(1450.00));
        testOrder.setStatus("NEW");
        testOrder.setIsDeleted(false);
        testOrder.setItems(new ArrayList<>());
    }

    @Test
    void getAll_ShouldReturnListOfOrders() {
        // Given
        when(orderRepository.findAllFiltered(null, null)).thenReturn(Arrays.asList(testOrder));

        // When
        List<OrderDto> result = orderService.getAll(null, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Иван Иванов", result.get(0).getCustomerName());
        assertEquals("+79991234567", result.get(0).getCustomerPhone());

        verify(orderRepository, times(1)).findAllFiltered(null, null);
    }

    @Test
    void getById_WhenOrderExists_ShouldReturnOrder() {
        // Given
        when(orderRepository.findByIdActive(1L)).thenReturn(Optional.of(testOrder));

        // When
        Optional<OrderDto> result = orderService.getById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Иван Иванов", result.get().getCustomerName());

        verify(orderRepository, times(1)).findByIdActive(1L);
    }

    @Test
    void updateStatus_WhenOrderExists_ShouldUpdateStatus() {
        // Given
        when(orderRepository.findByIdActive(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        Optional<OrderDto> result = orderService.updateStatus(1L, "COMPLETED");

        // Then
        assertTrue(result.isPresent());
        assertEquals("COMPLETED", testOrder.getStatus());

        verify(orderRepository, times(1)).findByIdActive(1L);
        verify(orderRepository, times(1)).save(testOrder);
    }

    @Test
    void softDelete_WhenOrderExists_ShouldReturnTrue() {
        // Given
        when(orderRepository.findByIdActive(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        boolean result = orderService.softDelete(1L);

        // Then
        assertTrue(result);
        assertTrue(testOrder.getIsDeleted());

        verify(orderRepository, times(1)).findByIdActive(1L);
        verify(orderRepository, times(1)).save(testOrder);
    }
}