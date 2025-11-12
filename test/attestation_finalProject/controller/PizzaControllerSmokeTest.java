package attestation_finalProject.controller;

import attestation_finalProject.service.PizzaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Простейший smoke-тест без поднятия Spring-контекста.
 * Проверяем, что PizzaController создаётся и готов к использованию.
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

    // Пример для будущих проверок эндпоинтов:
    // @Test
    // @DisplayName("GET /api/pizzas — базовый отклик (пример)")
    // void getAllPizzas_example() throws Exception {
    //     when(pizzaService.getAll()).thenReturn(Collections.emptyList());
    //     mockMvc.perform(get("/api/pizzas"))
    //            .andExpect(status().isOk());
    // }
}
