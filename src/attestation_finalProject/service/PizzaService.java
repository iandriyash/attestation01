package attestation_finalProject.service;

import attestation_finalProject.dto.PizzaDto;
import attestation_finalProject.entity.Pizza;
import attestation_finalProject.repository.PizzaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PizzaService {

    private final PizzaRepository pizzaRepository;

    // ===== Методы, которых ждут контроллеры =====

    /** Получить все пиццы (для контроллера) */
    @Transactional(readOnly = true)
    public List<PizzaDto> getAll() {
        return getAllPizzas();
    }

    /** Получить пиццу по ID -> Optional (для контроллера 200/404) */
    @Transactional(readOnly = true)
    public Optional<PizzaDto> getById(Long id) {
        return pizzaRepository.findByIdActive(id).map(this::convertToDto);
    }

    /** Создать пиццу (для контроллера) */
    @Transactional
    public PizzaDto create(PizzaDto dto) {
        return createPizza(dto);
    }

    /** Обновить пиццу -> Optional (для контроллера 200/404) */
    @Transactional
    public Optional<PizzaDto> update(Long id, PizzaDto dto) {
        return pizzaRepository.findByIdActive(id)
                .map(entity -> {
                    entity.setName(dto.getName());
                    entity.setDescription(dto.getDescription());
                    entity.setPrice(dto.getPrice());
                    return convertToDto(pizzaRepository.save(entity));
                });
    }

    /** Soft delete -> boolean (для контроллера 204/404) */
    @Transactional
    public boolean softDelete(Long id) {
        return pizzaRepository.findByIdActive(id)
                .map(entity -> {
                    entity.setIsDeleted(true);
                    pizzaRepository.save(entity);
                    return true;
                })
                .orElse(false);
    }

    // ===== ТВОИ исходные методы (сохраняем как есть) =====

    /** Получить все активные пиццы */
    @Transactional(readOnly = true)
    public List<PizzaDto> getAllPizzas() {
        log.debug("Получение всех активных пицц");
        return pizzaRepository.findAllActive().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /** Получить пиццу по ID (кидает исключение, если нет) */
    @Transactional(readOnly = true)
    public PizzaDto getPizzaById(Long id) {
        log.debug("Получение пиццы по ID: {}", id);
        Pizza pizza = pizzaRepository.findByIdActive(id)
                .orElseThrow(() -> new RuntimeException("Пицца с ID " + id + " не найдена"));
        return convertToDto(pizza);
    }

    /** Создать новую пиццу */
    @Transactional
    public PizzaDto createPizza(PizzaDto pizzaDto) {
        log.debug("Создание новой пиццы: {}", pizzaDto.getName());
        Pizza pizza = new Pizza();
        pizza.setName(pizzaDto.getName());
        pizza.setDescription(pizzaDto.getDescription());
        pizza.setPrice(pizzaDto.getPrice());
        pizza.setIsDeleted(false);
        return convertToDto(pizzaRepository.save(pizza));
    }

    /** Обновить пиццу (кидает исключение, если нет) */
    @Transactional
    public PizzaDto updatePizza(Long id, PizzaDto pizzaDto) {
        log.debug("Обновление пиццы ID: {}", id);
        Pizza pizza = pizzaRepository.findByIdActive(id)
                .orElseThrow(() -> new RuntimeException("Пицца с ID " + id + " не найдена"));
        pizza.setName(pizzaDto.getName());
        pizza.setDescription(pizzaDto.getDescription());
        pizza.setPrice(pizzaDto.getPrice());
        return convertToDto(pizzaRepository.save(pizza));
    }

    /** Удалить пиццу (Soft Delete) — кидает исключение, если нет */
    @Transactional
    public void deletePizza(Long id) {
        log.debug("Удаление пиццы ID: {}", id);
        Pizza pizza = pizzaRepository.findByIdActive(id)
                .orElseThrow(() -> new RuntimeException("Пицца с ID " + id + " не найдена"));
        pizza.setIsDeleted(true);
        pizzaRepository.save(pizza);
    }

    /** Поиск пицц по названию */
    @Transactional(readOnly = true)
    public List<PizzaDto> searchPizzasByName(String name) {
        log.debug("Поиск пицц по названию: {}", name);
        return pizzaRepository.findByNameContainingIgnoreCaseAndNotDeleted(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // === Вспомогательный маппер ===

    private PizzaDto convertToDto(Pizza pizza) {
        return new PizzaDto(
                pizza.getId(),
                pizza.getName(),
                pizza.getDescription(),
                pizza.getPrice()
        );
    }
}
