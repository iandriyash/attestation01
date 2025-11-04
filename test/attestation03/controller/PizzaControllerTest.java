package attestation.attestation03.controller;

import attestation.attestation03.dto.PizzaDto;
import attestation.attestation03.service.PizzaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PizzaController.class)
class PizzaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PizzaService pizzaService;

    private PizzaDto testPizza;

    @BeforeEach
    void setUp() {
        testPizza = new PizzaDto(1L, "Маргарита", "Томатный соус, моцарелла", BigDecimal.valueOf(450.00));
    }

    @Test
    void getAllPizzas_ShouldReturnListOfPizzas() throws Exception {
        List<PizzaDto> pizzas = Arrays.asList(testPizza);
        when(pizzaService.getAllPizzas()).thenReturn(pizzas);

        mockMvc.perform(get("/api/pizzas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Маргарита"))
                .andExpect(jsonPath("$[0].price").value(450.00));

        verify(pizzaService, times(1)).getAllPizzas();
    }

    @Test
    void getPizzaById_WhenExists_ShouldReturnPizza() throws Exception {
        when(pizzaService.getPizzaById(1L)).thenReturn(testPizza);

        mockMvc.perform(get("/api/pizzas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Маргарита"))
                .andExpect(jsonPath("$.price").value(450.00));

        verify(pizzaService, times(1)).getPizzaById(1L);
    }

    @Test
    void getPizzaById_WhenNotExists_ShouldReturn404() throws Exception {
        when(pizzaService.getPizzaById(999L)).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/api/pizzas/999"))
                .andExpect(status().isNotFound());

        verify(pizzaService, times(1)).getPizzaById(999L);
    }

    @Test
    void searchPizzas_ShouldReturnMatchingPizzas() throws Exception {
        List<PizzaDto> pizzas = Arrays.asList(testPizza);
        when(pizzaService.searchPizzasByName("Марга")).thenReturn(pizzas);

        mockMvc.perform(get("/api/pizzas/search")
                        .param("name", "Марга"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Маргарита"));

        verify(pizzaService, times(1)).searchPizzasByName("Марга");
    }

    @Test
    void createPizza_ShouldReturnCreatedPizza() throws Exception {
        PizzaDto newPizza = new PizzaDto(null, "Пепперони", "Острая", BigDecimal.valueOf(550.00));
        PizzaDto savedPizza = new PizzaDto(2L, "Пепперони", "Острая", BigDecimal.valueOf(550.00));

        when(pizzaService.createPizza(any(PizzaDto.class))).thenReturn(savedPizza);

        mockMvc.perform(post("/api/pizzas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPizza)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Пепперони"));

        verify(pizzaService, times(1)).createPizza(any(PizzaDto.class));
    }

    @Test
    void updatePizza_WhenExists_ShouldReturnUpdatedPizza() throws Exception {
        PizzaDto updatedPizza = new PizzaDto(1L, "Маргарита Обновлённая", "Новое описание", BigDecimal.valueOf(500.00));

        when(pizzaService.updatePizza(eq(1L), any(PizzaDto.class))).thenReturn(updatedPizza);

        mockMvc.perform(put("/api/pizzas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPizza)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Маргарита Обновлённая"));

        verify(pizzaService, times(1)).updatePizza(eq(1L), any(PizzaDto.class));
    }

    @Test
    void deletePizza_WhenExists_ShouldReturn204() throws Exception {
        doNothing().when(pizzaService).deletePizza(1L);

        mockMvc.perform(delete("/api/pizzas/1"))
                .andExpect(status().isNoContent());

        verify(pizzaService, times(1)).deletePizza(1L);
    }

    @Test
    void deletePizza_WhenNotExists_ShouldReturn404() throws Exception {
        doThrow(new RuntimeException("Not found")).when(pizzaService).deletePizza(999L);

        mockMvc.perform(delete("/api/pizzas/999"))
                .andExpect(status().isNotFound());

        verify(pizzaService, times(1)).deletePizza(999L);
    }
}