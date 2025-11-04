package attestation.attestation03.controller;

import attestation.attestation03.dto.PizzaDto;
import attestation.attestation03.service.PizzaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
@RequiredArgsConstructor
@Slf4j
public class PizzaController {

    private final PizzaService pizzaService;

    /**
     * GET /api/pizzas - Получить все пиццы
     */
    @GetMapping
    public ResponseEntity<List<PizzaDto>> getAllPizzas() {
        log.info("GET /api/pizzas - Получение всех пицц");
        List<PizzaDto> pizzas = pizzaService.getAllPizzas();
        return ResponseEntity.ok(pizzas);
    }

    /**
     * GET /api/pizzas/{id} - Получить пиццу по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PizzaDto> getPizzaById(@PathVariable Long id) {
        log.info("GET /api/pizzas/{} - Получение пиццы по ID", id);
        try {
            PizzaDto pizza = pizzaService.getPizzaById(id);
            return ResponseEntity.ok(pizza);
        } catch (RuntimeException e) {
            log.error("Пицца с ID {} не найдена", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/pizzas/search?name=... - Поиск пицц по названию
     */
    @GetMapping("/search")
    public ResponseEntity<List<PizzaDto>> searchPizzas(@RequestParam String name) {
        log.info("GET /api/pizzas/search?name={} - Поиск пицц", name);
        List<PizzaDto> pizzas = pizzaService.searchPizzasByName(name);
        return ResponseEntity.ok(pizzas);
    }

    /**
     * POST /api/pizzas - Создать новую пиццу
     */
    @PostMapping
    public ResponseEntity<PizzaDto> createPizza(@RequestBody PizzaDto pizzaDto) {
        log.info("POST /api/pizzas - Создание новой пиццы: {}", pizzaDto.getName());
        PizzaDto created = pizzaService.createPizza(pizzaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/pizzas/{id} - Обновить пиццу
     */
    @PutMapping("/{id}")
    public ResponseEntity<PizzaDto> updatePizza(@PathVariable Long id, @RequestBody PizzaDto pizzaDto) {
        log.info("PUT /api/pizzas/{} - Обновление пиццы", id);
        try {
            PizzaDto updated = pizzaService.updatePizza(id, pizzaDto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("Пицца с ID {} не найдена", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/pizzas/{id} - Удалить пиццу (Soft Delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePizza(@PathVariable Long id) {
        log.info("DELETE /api/pizzas/{} - Удаление пиццы", id);
        try {
            pizzaService.deletePizza(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Пицца с ID {} не найдена", id);
            return ResponseEntity.notFound().build();
        }
    }
}