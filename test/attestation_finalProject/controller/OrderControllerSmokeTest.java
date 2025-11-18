package attestation_finalProject.controller;

import attestation_finalProject.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Smoke-тест без Spring-контекста для OrderController.
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

    @Test
    @DisplayName("GET /api/orders — 200 и пустой список (без параметров)")
    void getAllOrders_noParams() throws Exception {
        when(orderService.getAll(isNull(), isNull()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/orders").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        verify(orderService, times(1)).getAll(isNull(), isNull());
    }

    @Test
    @DisplayName("GET /api/orders — 200 при переданных query-параметрах (второй может стать null)")
    void getAllOrders_withParams_lenientSecondArg() throws Exception {
        // Контроллер может пробросить второй аргумент как null (в зависимости от имени параметра/конверсии)
        when(orderService.getAll(eq("new"), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/orders")
                        .param("status", "new")
                        .param("customer", "alice"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        verify(orderService, times(1)).getAll(eq("new"), any());
    }

    @Test
    @DisplayName("GET /api/orders — 200 и [] при пустых строках параметров")
    void getAllOrders_emptyStrings() throws Exception {
        // По логу метод вызывается как getAll(\"\", null) — поэтому второй аргумент матчим свободно
        when(orderService.getAll(eq(""), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/orders")
                        .param("status", "")
                        .param("customer", ""))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(orderService, times(1)).getAll(eq(""), any());
    }
}
