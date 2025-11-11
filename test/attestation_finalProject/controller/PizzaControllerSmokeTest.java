package attestation_finalProject.controller;

import attestation_finalProject.dto.PizzaDto;
import attestation_finalProject.service.PizzaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Smoke-тест для PizzaController.
 * Проверяем, что эндпоинты существуют и отдают корректный статус.
 */
@WebMvcTest(controllers = PizzaController.class)
class PizzaControllerSmokeTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PizzaService pizzaService;

    @Test
    @WithMockUser
    void getAllPizzas_ShouldReturn200() throws Exception {
        // Given
        PizzaDto pizza = new PizzaDto(1L, "Маргарита", "Классика", BigDecimal.valueOf(450));
        when(pizzaService.getAll()).thenReturn(Arrays.asList(pizza));

        // When & Then
        mockMvc.perform(get("/api/pizzas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Маргарита"));
    }

    @Test
    @WithMockUser
    void getPizzaById_WhenExists_ShouldReturn200() throws Exception {
        // Given
        PizzaDto pizza = new PizzaDto(1L, "Маргарита", "Классика", BigDecimal.valueOf(450));
        when(pizzaService.getById(1L)).thenReturn(Optional.of(pizza));

        // When & Then
        mockMvc.perform(get("/api/pizzas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Маргарита"))
                .andExpect(jsonPath("$.price").value(450.0));
    }

    @Test
    @WithMockUser
    void getPizzaById_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        when(pizzaService.getById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/pizzas/999"))
                .andExpect(status().isNotFound());
    }
}