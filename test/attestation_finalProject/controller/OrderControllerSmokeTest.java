package attestation_finalProject.controller;

import attestation_finalProject.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Простейший smoke-тест без Spring-контекста.
 * Проверяем, что OrderController создаётся и готов к использованию.
 */
@ExtendWith(MockitoExtension.class)
class OrderControllerSmokeTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("OrderController создаётся (smoke)")
    void controller_isCreated() {
        assertThat(controller).isNotNull();
    }

    // При необходимости можно добавить пример запроса:
    // @Test
    // @DisplayName("GET /api/orders — базовый отклик (пример)")
    // void getAllOrders_example() throws Exception {
    //     when(orderService.getAll()).thenReturn(Collections.emptyList());
    //     mockMvc.perform(get("/api/orders"))
    //            .andExpect(status().isOk());
    // }
}
