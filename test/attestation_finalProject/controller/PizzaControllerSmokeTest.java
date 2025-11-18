package attestation_finalProject.controller;

import attestation_finalProject.service.PizzaService;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Простейшие smoke-тесты PizzaController без поднятия контекста.
 */
@ExtendWith(MockitoExtension.class)
class PizzaControllerSmokeTest {

    @Mock
    private PizzaService pizzaService;

    @InjectMocks
    private PizzaController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("PizzaController создаётся (smoke)")
    void controller_isCreated() {
        assertThat(controller).isNotNull();
    }

    @Test
    @DisplayName("GET /api/pizzas — 200 и пустой список")
    void getAllPizzas_noParams_returnsEmptyList() throws Exception {
        // предполагаем сигнатуру pizzaService.getAll() без аргументов
        when(pizzaService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/pizzas").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        verify(pizzaService, times(1)).getAll();
    }
}
