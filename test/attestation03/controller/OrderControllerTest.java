package attestation.attestation03.controller;

import attestation.attestation03.dto.CreateOrderRequest;
import attestation.attestation03.dto.OrderDto;
import attestation.attestation03.dto.OrderItemDto;
import attestation.attestation03.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
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
        OrderItemDto item1 = new OrderItemDto(1L, 1L, "Маргарита", 2, BigDecimal.valueOf(450.00));
        OrderItemDto item2 = new OrderItemDto(2L, 2L, "Пепперони", 1, BigDecimal.valueOf(550.00));

        testOrder = new OrderDto(
                1L,
                "Иван Иванов",
                "+79991234567",
                BigDecimal.valueOf(1450.00),
                "NEW",
                LocalDateTime.now(),
                Arrays.asList(item1, item2)
        );
    }

    @Test
    void getAllOrders_ShouldReturnListOfOrders() throws Exception {
        List<OrderDto> orders = Arrays.asList(testOrder);
        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("Иван Иванов"))
                .andExpect(jsonPath("$[0].totalPrice").value(1450.00));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void getAllOrders_WithStatusFilter_ShouldReturnFilteredOrders() throws Exception {
        List<OrderDto> orders = Arrays.asList(testOrder);
        when(orderService.getOrdersByStatus("NEW")).thenReturn(orders);

        mockMvc.perform(get("/api/orders")
                        .param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("NEW"));

        verify(orderService, times(1)).getOrdersByStatus("NEW");
    }

    @Test
    void getAllOrders_WithPhoneFilter_ShouldReturnFilteredOrders() throws Exception {
        List<OrderDto> orders = Arrays.asList(testOrder);
        when(orderService.getOrdersByPhone("+79991234567")).thenReturn(orders);

        mockMvc.perform(get("/api/orders")
                        .param("phone", "+79991234567"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerPhone").value("+79991234567"));

        verify(orderService, times(1)).getOrdersByPhone("+79991234567");
    }

    @Test
    void getOrderById_WhenExists_ShouldReturnOrder() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(testOrder);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Иван Иванов"))
                .andExpect(jsonPath("$.totalPrice").value(1450.00));

        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    void getOrderById_WhenNotExists_ShouldReturn404() throws Exception {
        when(orderService.getOrderById(999L)).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/api/orders/999"))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).getOrderById(999L);
    }

    @Test
    void createOrder_ShouldReturnCreatedOrder() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerName("Петр Петров");
        request.setCustomerPhone("+79991234568");

        CreateOrderRequest.OrderItemRequest itemRequest = new CreateOrderRequest.OrderItemRequest();
        itemRequest.setPizzaId(1L);
        itemRequest.setQuantity(1);
        request.setItems(Arrays.asList(itemRequest));

        when(orderService.createOrder(any(CreateOrderRequest.class))).thenReturn(testOrder);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("Иван Иванов"));

        verify(orderService, times(1)).createOrder(any(CreateOrderRequest.class));
    }

    @Test
    void createOrder_WithInvalidData_ShouldReturn400() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerName("Петр Петров");
        request.setCustomerPhone("+79991234568");
        request.setItems(Arrays.asList());

        when(orderService.createOrder(any(CreateOrderRequest.class)))
                .thenThrow(new RuntimeException("Invalid data"));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(orderService, times(1)).createOrder(any(CreateOrderRequest.class));
    }

    @Test
    void updateOrderStatus_WhenExists_ShouldReturnUpdatedOrder() throws Exception {
        OrderDto updatedOrder = new OrderDto(
                1L,
                "Иван Иванов",
                "+79991234567",
                BigDecimal.valueOf(1450.00),
                "COMPLETED",
                LocalDateTime.now(),
                testOrder.getItems()
        );

        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", "COMPLETED");

        when(orderService.updateOrderStatus(eq(1L), eq("COMPLETED"))).thenReturn(updatedOrder);

        mockMvc.perform(patch("/api/orders/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(orderService, times(1)).updateOrderStatus(eq(1L), eq("COMPLETED"));
    }

    @Test
    void updateOrderStatus_WhenNotExists_ShouldReturn404() throws Exception {
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", "COMPLETED");

        when(orderService.updateOrderStatus(eq(999L), eq("COMPLETED")))
                .thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(patch("/api/orders/999/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).updateOrderStatus(eq(999L), eq("COMPLETED"));
    }

    @Test
    void deleteOrder_WhenExists_ShouldReturn204() throws Exception {
        doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrder(1L);
    }

    @Test
    void deleteOrder_WhenNotExists_ShouldReturn404() throws Exception {
        doThrow(new RuntimeException("Not found")).when(orderService).deleteOrder(999L);

        mockMvc.perform(delete("/api/orders/999"))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).deleteOrder(999L);
    }
}