package attestation_finalProject.controller;

import attestation_finalProject.dto.CreateOrderRequest;
import attestation_finalProject.dto.OrderDto;
import attestation_finalProject.dto.OrderItemDto;
import attestation_finalProject.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Smoke-тест для OrderController.
 * Проверяем создание заказа (POST) и получение заказа (GET).
 */
@WebMvcTest(controllers = OrderController.class)
class OrderControllerSmokeTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser
    void createOrder_ShouldReturn201Created() throws Exception {
        // Given - подготовим запрос на создание заказа
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerName("Иван Иванов");
        request.setCustomerPhone("+79991234567");

        OrderItemDto item = new OrderItemDto(1L, 2, BigDecimal.valueOf(450));
        request.setItems(Arrays.asList(item));

        // Given - мокаем ответ сервиса
        OrderDto createdOrder = new OrderDto(
                1L,
                "Иван Иванов",
                "+79991234567",
                BigDecimal.valueOf(900),
                "NEW",
                LocalDateTime.now(),
                Arrays.asList(item)
        );
        when(orderService.create(any(CreateOrderRequest.class))).thenReturn(createdOrder);

        // When & Then
        mockMvc.perform(post("/api/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("Иван Иванов"))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    @WithMockUser
    void getOrderById_WhenExists_ShouldReturn200() throws Exception {
        // Given
        OrderItemDto item = new OrderItemDto(1L, 2, BigDecimal.valueOf(450));
        OrderDto order = new OrderDto(
                1L,
                "Иван Иванов",
                "+79991234567",
                BigDecimal.valueOf(900),
                "NEW",
                LocalDateTime.now(),
                Arrays.asList(item)
        );
        when(orderService.getById(1L)).thenReturn(Optional.of(order));

        // When & Then
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Иван Иванов"))
                .andExpect(jsonPath("$.totalPrice").value(900.0));
    }

    @Test
    @WithMockUser
    void getAllOrders_ShouldReturn200() throws Exception {
        // Given
        OrderItemDto item = new OrderItemDto(1L, 2, BigDecimal.valueOf(450));
        OrderDto order = new OrderDto(
                1L,
                "Иван Иванов",
                "+79991234567",
                BigDecimal.valueOf(900),
                "NEW",
                LocalDateTime.now(),
                Arrays.asList(item)
        );
        when(orderService.getAll(null, null)).thenReturn(Arrays.asList(order));

        // When & Then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].customerName").value("Иван Иванов"));
    }
}