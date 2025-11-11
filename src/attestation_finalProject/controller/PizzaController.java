package attestation_finalProject.controller;

import attestation_finalProject.dto.PizzaDto;
import attestation_finalProject.service.PizzaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Pizzas", description = "Операции с пиццами")
@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {

    private final PizzaService pizzaService;

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @Operation(summary = "Получить список пицц")
    @ApiResponse(responseCode = "200", description = "Список пицц")
    @GetMapping
    public ResponseEntity<List<PizzaDto>> getAll() {
        return ResponseEntity.ok(pizzaService.getAll());
    }

    @Operation(
            summary = "Получить пиццу по ID",
            description = "Возвращает пиццу с учётом soft delete"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пицца найдена",
                    content = @Content(schema = @Schema(implementation = PizzaDto.class))),
            @ApiResponse(responseCode = "404", description = "Пицца не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PizzaDto> getById(@PathVariable Long id) {
        // 200 с телом либо 404 — без конфликтов дженериков
        return ResponseEntity.of(pizzaService.getById(id));
    }

    @Operation(summary = "Создать пиццу")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пицца создана"),
            @ApiResponse(responseCode = "400", description = "Неверные данные")
    })
    @PostMapping
    public ResponseEntity<PizzaDto> create(@RequestBody PizzaDto dto) {
        PizzaDto created = pizzaService.create(dto);
        return ResponseEntity
                .created(URI.create("/api/pizzas/" + created.getId()))
                .body(created);
    }

    @Operation(summary = "Обновить пиццу")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пицца обновлена"),
            @ApiResponse(responseCode = "404", description = "Пицца не найдена")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PizzaDto> update(@PathVariable Long id, @RequestBody PizzaDto dto) {
        return ResponseEntity.of(pizzaService.update(id, dto));
    }

    @Operation(summary = "Удалить пиццу (soft delete)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Удалено"),
            @ApiResponse(responseCode = "404", description = "Пицца не найдена")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = pizzaService.softDelete(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
