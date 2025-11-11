package attestation_finalProject.controller;

import attestation_finalProject.dto.PizzaDto;
import attestation_finalProject.service.PizzaService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = attestation_finalProject.controller.PizzaController.class)
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
        when(pizzaService.getAll()).thenReturn(pizzas);

        mockMvc.perform(get("/api/pizzas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Маргарита"))
                .andExpect(jsonPath("$[0].price").value(450.00));

        verify(pizzaService, times(1)).getAll();
    }

    @Test
    void getPizzaById_WhenExists_ShouldReturnPizza() throws Exception {
        when(pizzaService.getById(1L)).thenReturn(Optional.of(testPizza));

        mockMvc.perform(get("/api/pizzas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Маргарита"))
                .andExpect(jsonPath("$.price").value(450.00));

        verify(pizzaService, times(1)).getById(1L);
    }

    @Test
    void getPizzaById_WhenNotExists_ShouldReturn404() throws Exception {
        when(pizzaService.getById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pizzas/999"))
                .andExpect(status().isNotFound());

        verify(pizzaService, times(1)).getById(999L);
    }

    @Test
    void createPizza_ShouldReturnCreatedPizza() throws Exception {
        PizzaDto newPizza = new PizzaDto(null, "Пепперони", "Острая", BigDecimal.valueOf(550.00));
        PizzaDto savedPizza = new PizzaDto(2L, "Пепперони", "Острая", BigDecimal.valueOf(550.00));

        when(pizzaService.create(any(PizzaDto.class))).thenReturn(savedPizza);

        mockMvc.perform(post("/api/pizzas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPizza)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Пепперони"));

        verify(pizzaService, times(1)).create(any(PizzaDto.class));
    }

    @Test
    void updatePizza_WhenExists_ShouldReturnUpdatedPizza() throws Exception {
        PizzaDto updatedPizza = new PizzaDto(1L, "Маргарита Обновлённая", "Новое описание", BigDecimal.valueOf(500.00));

        when(pizzaService.update(eq(1L), any(PizzaDto.class))).thenReturn(Optional.of(updatedPizza));

        mockMvc.perform(put("/api/pizzas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPizza)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Маргарита Обновлённая"));

        verify(pizzaService, times(1)).update(eq(1L), any(PizzaDto.class));
    }

    @Test
    void deletePizza_WhenExists_ShouldReturn204() throws Exception {
        when(pizzaService.softDelete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/pizzas/1"))
                .andExpect(status().isNoContent());

        verify(pizzaService, times(1)).softDelete(1L);
    }

    @Test
    void deletePizza_WhenNotExists_ShouldReturn404() throws Exception {
        when(pizzaService.softDelete(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/pizzas/999"))
                .andExpect(status().isNotFound());

        verify(pizzaService, times(1)).softDelete(999L);
    }
}