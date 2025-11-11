package attestation_finalProject.service;

import attestation_finalProject.dto.CreateOrderRequest;
import attestation_finalProject.dto.OrderDto;
import attestation_finalProject.dto.OrderItemDto;
import attestation_finalProject.entity.Order;
import attestation_finalProject.entity.Pizza;
import attestation_finalProject.repository.OrderItemRepository;
import attestation_finalProject.repository.OrderRepository;
import attestation_finalProject.repository.PizzaRepository;
import attestation_finalProject.service.OrderService;
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
    private Pizza testPizza;

    @BeforeEach
    void setUp() {
        testPizza = new Pizza();
        testPizza.setId(1L);
        testPizza.setName("Маргарита");
        testPizza.setPrice(BigDecimal.valueOf(450.00));
        testPizza.setIsDeleted(false);

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setCustomerName("Иван Иванов");
        testOrder.setCustomerPhone("+79991234567");
        testOrder.setTotalPrice(BigDecimal.valueOf(900.00));
        testOrder.setStatus("NEW");
        testOrder.setIsDeleted(false);
        testOrder.setItems(new ArrayList<>());
    }

    @Test
    void getAllOrders_ShouldReturnListOfOrders() {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findAllFiltered(null, null)).thenReturn(orders);

        List<OrderDto> result = orderService.getAll(null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Иван Иванов", result.get(0).getCustomerName());
        verify(orderRepository, times(1)).findAllFiltered(null, null);
    }

    @Test
    void getOrderById_WhenExists_ShouldReturnOrder() {
        when(orderRepository.findByIdActive(1L)).thenReturn(Optional.of(testOrder));

        Optional<OrderDto> result = orderService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("Иван Иванов", result.get().getCustomerName());
        verify(orderRepository, times(1)).findByIdActive(1L);
    }

    @Test
    void createOrder_ShouldReturnCreatedOrder() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerName("Иван Иванов");
        request.setCustomerPhone("+79991234567");

        OrderItemDto itemDto = new OrderItemDto(1L, 2, BigDecimal.valueOf(450.00));
        request.setItems(Arrays.asList(itemDto));

        when(pizzaRepository.findByIdActive(1L)).thenReturn(Optional.of(testPizza));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        OrderDto result = orderService.create(request);

        assertNotNull(result);
        assertEquals("Иван Иванов", result.getCustomerName());
        verify(pizzaRepository, times(1)).findByIdActive(1L);
        verify(orderRepository, times(2)).save(any(Order.class));
    }

    @Test
    void updateOrderStatus_WhenExists_ShouldReturnUpdatedOrder() {
        when(orderRepository.findByIdActive(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Optional<OrderDto> result = orderService.updateStatus(1L, "COMPLETED");

        assertTrue(result.isPresent());
        verify(orderRepository, times(1)).findByIdActive(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void deleteOrder_WhenExists_ShouldSetDeletedFlag() {
        when(orderRepository.findByIdActive(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        boolean result = orderService.softDelete(1L);

        assertTrue(result);
        verify(orderRepository, times(1)).findByIdActive(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getOrdersByStatus_ShouldReturnFilteredOrders() {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findAllFiltered("NEW", null)).thenReturn(orders);

        List<OrderDto> result = orderService.getAll("NEW", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAllFiltered("NEW", null);
    }
}