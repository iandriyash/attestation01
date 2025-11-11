package attestation_finalProject.controller;

import attestation_finalProject.dto.CreateOrderRequest;
import attestation_finalProject.dto.OrderDto;
import attestation_finalProject.dto.OrderItemDto;
import attestation_finalProject.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = attestation_finalProject.controller.OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private OrderDto testOrder;

    @BeforeEach
    void setUp() {
        OrderItemDto item1 = new OrderItemDto(1L, 2, BigDecimal.valueOf(450.00));
        OrderItemDto item2 = new OrderItemDto(2L, 1, BigDecimal.valueOf(550.00));

        testOrder = new OrderDto(
                1L,
                "Иван Иванов",
                "+79991234567",
                BigDecimal.valueOf(1450.00),
                "NEW",
                Instant.now(),
                Arrays.asList(item1, item2)
        );
    }

    @Test
    void getAllOrders_ShouldReturnListOfOrders() throws Exception {
        List<OrderDto> orders = Arrays.asList(testOrder);
        when(orderService.getAll(null, null)).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("Иван Иванов"))
                .andExpect(jsonPath("$[0].totalPrice").value(1450.00));

        verify(orderService, times(1)).getAll(null, null);
    }

    @Test
    void getAllOrders_WithStatusFilter_ShouldReturnFilteredOrders() throws Exception {
        List<OrderDto> orders = Arrays.asList(testOrder);
        when(orderService.getAll("NEW", null)).thenReturn(orders);

        mockMvc.perform(get("/api/orders")
                        .param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("NEW"));

        verify(orderService, times(1)).getAll("NEW", null);
    }

    @Test
    void getAllOrders_WithPhoneFilter_ShouldReturnFilteredOrders() throws Exception {
        List<OrderDto> orders = Arrays.asList(testOrder);
        when(orderService.getAll(null, "+79991234567")).thenReturn(orders);

        mockMvc.perform(get("/api/orders")
                        .param("phone", "+79991234567"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerPhone").value("+79991234567"));

        verify(orderService, times(1)).getAll(null, "+79991234567");
    }

    @Test
    void getOrderById_WhenExists_ShouldReturnOrder() throws Exception {
        when(orderService.getById(1L)).thenReturn(Optional.of(testOrder));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Иван Иванов"))
                .andExpect(jsonPath("$.totalPrice").value(1450.00));

        verify(orderService, times(1)).getById(1L);
    }

    @Test
    void getOrderById_WhenNotExists_ShouldReturn404() throws Exception {
        when(orderService.getById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/999"))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).getById(999L);
    }

    @Test
    void createOrder_ShouldReturnCreatedOrder() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerName("Петр Петров");
        request.setCustomerPhone("+79991234568");

        OrderItemDto itemDto = new OrderItemDto(1L, 1, BigDecimal.valueOf(450.00));
        request.setItems(Arrays.asList(itemDto));

        when(orderService.create(any(CreateOrderRequest.class))).thenReturn(testOrder);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("Иван Иванов"));

        verify(orderService, times(1)).create(any(CreateOrderRequest.class));
    }

    @Test
    void updateOrderStatus_WhenExists_ShouldReturnUpdatedOrder() throws Exception {
        OrderDto updatedOrder = new OrderDto(
                1L,
                "Иван Иванов",
                "+79991234567",
                BigDecimal.valueOf(1450.00),
                "COMPLETED",
                Instant.now(),
                testOrder.getItems()
        );

        when(orderService.updateStatus(eq(1L), eq("COMPLETED"))).thenReturn(Optional.of(updatedOrder));

        mockMvc.perform(patch("/api/orders/1/status")
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(orderService, times(1)).updateStatus(eq(1L), eq("COMPLETED"));
    }

    @Test
    void updateOrderStatus_WhenNotExists_ShouldReturn404() throws Exception {
        when(orderService.updateStatus(eq(999L), eq("COMPLETED")))
                .thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/orders/999/status")
                        .param("status", "COMPLETED"))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).updateStatus(eq(999L), eq("COMPLETED"));
    }

    @Test
    void deleteOrder_WhenExists_ShouldReturn204() throws Exception {
        when(orderService.softDelete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).softDelete(1L);
    }

    @Test
    void deleteOrder_WhenNotExists_ShouldReturn404() throws Exception {
        when(orderService.softDelete(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/orders/999"))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).softDelete(999L);
    }
}