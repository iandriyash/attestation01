package attestation_finalProject.service;

import attestation_finalProject.dto.PizzaDto;
import attestation_finalProject.entity.Pizza;
import attestation_finalProject.repository.PizzaRepository;
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

/**
 * Unit-тесты для PizzaService.
 * Используем чистый Mockito без Spring-контекста.
 */
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
        testPizza.setDescription("Классическая пицца");
        testPizza.setPrice(BigDecimal.valueOf(450.00));
        testPizza.setIsDeleted(false);
    }

    @Test
    void getAll_ShouldReturnListOfActivePizzas() {
        // Given
        when(pizzaRepository.findAllActive()).thenReturn(Arrays.asList(testPizza));

        // When
        List<PizzaDto> result = pizzaService.getAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Маргарита", result.get(0).getName());
        assertEquals(BigDecimal.valueOf(450.00), result.get(0).getPrice());

        verify(pizzaRepository, times(1)).findAllActive();
    }

    @Test
    void getById_WhenPizzaExists_ShouldReturnPizza() {
        // Given
        when(pizzaRepository.findByIdActive(1L)).thenReturn(Optional.of(testPizza));

        // When
        Optional<PizzaDto> result = pizzaService.getById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Маргарита", result.get().getName());

        verify(pizzaRepository, times(1)).findByIdActive(1L);
    }

    @Test
    void getById_WhenPizzaNotExists_ShouldReturnEmpty() {
        // Given
        when(pizzaRepository.findByIdActive(999L)).thenReturn(Optional.empty());

        // When
        Optional<PizzaDto> result = pizzaService.getById(999L);

        // Then
        assertFalse(result.isPresent());

        verify(pizzaRepository, times(1)).findByIdActive(999L);
    }

    @Test
    void softDelete_WhenPizzaExists_ShouldReturnTrue() {
        // Given
        when(pizzaRepository.findByIdActive(1L)).thenReturn(Optional.of(testPizza));
        when(pizzaRepository.save(any(Pizza.class))).thenReturn(testPizza);

        // When
        boolean result = pizzaService.softDelete(1L);

        // Then
        assertTrue(result);
        assertTrue(testPizza.getIsDeleted());

        verify(pizzaRepository, times(1)).findByIdActive(1L);
        verify(pizzaRepository, times(1)).save(testPizza);
    }

    @Test
    void softDelete_WhenPizzaNotExists_ShouldReturnFalse() {
        // Given
        when(pizzaRepository.findByIdActive(999L)).thenReturn(Optional.empty());

        // When
        boolean result = pizzaService.softDelete(999L);

        // Then
        assertFalse(result);

        verify(pizzaRepository, times(1)).findByIdActive(999L);
        verify(pizzaRepository, never()).save(any());
    }
}