package attestation.attestation03.service;

import attestation.attestation03.dto.PizzaDto;
import attestation.attestation03.entity.Pizza;
import attestation.attestation03.repository.PizzaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PizzaService {

    private final PizzaRepository pizzaRepository;

    /**
     * Получить все активные пиццы
     */
    @Transactional(readOnly = true)
    public List<PizzaDto> getAllPizzas() {
        log.debug("Получение всех активных пицц");
        return pizzaRepository.findAllActive().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить пиццу по ID
     */
    @Transactional(readOnly = true)
    public PizzaDto getPizzaById(Long id) {
        log.debug("Получение пиццы по ID: {}", id);
        Pizza pizza = pizzaRepository.findByIdActive(id)
                .orElseThrow(() -> new RuntimeException("Пицца с ID " + id + " не найдена"));
        return convertToDto(pizza);
    }

    /**
     * Создать новую пиццу
     */
    @Transactional
    public PizzaDto createPizza(PizzaDto pizzaDto) {
        log.debug("Создание новой пиццы: {}", pizzaDto.getName());
        Pizza pizza = new Pizza();
        pizza.setName(pizzaDto.getName());
        pizza.setDescription(pizzaDto.getDescription());
        pizza.setPrice(pizzaDto.getPrice());
        pizza.setIsDeleted(false);

        Pizza saved = pizzaRepository.save(pizza);
        return convertToDto(saved);
    }

    /**
     * Обновить пиццу
     */
    @Transactional
    public PizzaDto updatePizza(Long id, PizzaDto pizzaDto) {
        log.debug("Обновление пиццы ID: {}", id);
        Pizza pizza = pizzaRepository.findByIdActive(id)
                .orElseThrow(() -> new RuntimeException("Пицца с ID " + id + " не найдена"));

        pizza.setName(pizzaDto.getName());
        pizza.setDescription(pizzaDto.getDescription());
        pizza.setPrice(pizzaDto.getPrice());

        Pizza updated = pizzaRepository.save(pizza);
        return convertToDto(updated);
    }

    /**
     * Удалить пиццу (Soft Delete)
     */
    @Transactional
    public void deletePizza(Long id) {
        log.debug("Удаление пиццы ID: {}", id);
        Pizza pizza = pizzaRepository.findByIdActive(id)
                .orElseThrow(() -> new RuntimeException("Пицца с ID " + id + " не найдена"));

        pizza.setIsDeleted(true);
        pizzaRepository.save(pizza);
    }

    /**
     * Поиск пицц по названию
     */
    @Transactional(readOnly = true)
    public List<PizzaDto> searchPizzasByName(String name) {
        log.debug("Поиск пицц по названию: {}", name);
        return pizzaRepository.findByNameContainingIgnoreCaseAndNotDeleted(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // === Вспомогательные методы ===

    private PizzaDto convertToDto(Pizza pizza) {
        return new PizzaDto(
                pizza.getId(),
                pizza.getName(),
                pizza.getDescription(),
                pizza.getPrice()
        );
    }
}