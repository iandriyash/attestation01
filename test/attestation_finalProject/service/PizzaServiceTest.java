package attestation_finalProject.service;

import attestation_finalProject.dto.PizzaDto;
import attestation_finalProject.entity.Pizza;
import attestation_finalProject.repository.PizzaRepository;
import attestation_finalProject.service.PizzaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PizzaServiceTest {

    @Mock
    private PizzaRepository pizzaRepository;

    @InjectMocks
    private PizzaService pizzaService;

    private Pizza testPizza;

    @BeforeEach
    void setUp() {
        testPizza = new Pizza();
        testPizza.setId(1L);
        testPizza.setName("Маргарита");
        testPizza.setDescription("Томатный соус, моцарелла");
        testPizza.setPrice(BigDecimal.valueOf(450.00));
        testPizza.setIsDeleted(false);
    }

    @Test
    void getAllPizzas_ShouldReturnListOfPizzas() {
        List<Pizza> pizzas = Arrays.asList(testPizza);
        when(pizzaRepository.findAllActive()).thenReturn(pizzas);

        List<PizzaDto> result = pizzaService.getAllPizzas();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Маргарита", result.get(0).getName());
        verify(pizzaRepository, times(1)).findAllActive();
    }

    @Test
    void getPizzaById_WhenExists_ShouldReturnPizza() {
        when(pizzaRepository.findByIdActive(1L)).thenReturn(Optional.of(testPizza));

        PizzaDto result = pizzaService.getPizzaById(1L);

        assertNotNull(result);
        assertEquals("Маргарита", result.getName());
        assertEquals(BigDecimal.valueOf(450.00), result.getPrice());
        verify(pizzaRepository, times(1)).findByIdActive(1L);
    }

    @Test
    void getPizzaById_WhenNotExists_ShouldThrowException() {
        when(pizzaRepository.findByIdActive(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pizzaService.getPizzaById(999L));
        verify(pizzaRepository, times(1)).findByIdActive(999L);
    }

    @Test
    void createPizza_ShouldReturnCreatedPizza() {
        PizzaDto pizzaDto = new PizzaDto(null, "Пепперони", "Острая пицца", BigDecimal.valueOf(550.00));
        when(pizzaRepository.save(any(Pizza.class))).thenReturn(testPizza);

        PizzaDto result = pizzaService.createPizza(pizzaDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        verify(pizzaRepository, times(1)).save(any(Pizza.class));
    }

    @Test
    void updatePizza_WhenExists_ShouldReturnUpdatedPizza() {
        PizzaDto updateDto = new PizzaDto(1L, "Обновленная Маргарита", "Новое описание", BigDecimal.valueOf(500.00));
        when(pizzaRepository.findByIdActive(1L)).thenReturn(Optional.of(testPizza));
        when(pizzaRepository.save(any(Pizza.class))).thenReturn(testPizza);

        PizzaDto result = pizzaService.updatePizza(1L, updateDto);

        assertNotNull(result);
        verify(pizzaRepository, times(1)).findByIdActive(1L);
        verify(pizzaRepository, times(1)).save(any(Pizza.class));
    }

    @Test
    void deletePizza_WhenExists_ShouldSetDeletedFlag() {
        when(pizzaRepository.findByIdActive(1L)).thenReturn(Optional.of(testPizza));
        when(pizzaRepository.save(any(Pizza.class))).thenReturn(testPizza);

        pizzaService.deletePizza(1L);

        verify(pizzaRepository, times(1)).findByIdActive(1L);
        verify(pizzaRepository, times(1)).save(any(Pizza.class));
    }

    @Test
    void searchPizzasByName_ShouldReturnMatchingPizzas() {
        List<Pizza> pizzas = Arrays.asList(testPizza);
        when(pizzaRepository.findByNameContainingIgnoreCaseAndNotDeleted("Марга")).thenReturn(pizzas);

        List<PizzaDto> result = pizzaService.searchPizzasByName("Марга");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(pizzaRepository, times(1)).findByNameContainingIgnoreCaseAndNotDeleted("Марга");
    }
}