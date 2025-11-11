package attestation_finalProject.controller;

import attestation_finalProject.dto.CreateOrderRequest;
import attestation_finalProject.dto.OrderDto;
import attestation_finalProject.service.OrderService;
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

@Tag(name = "Orders", description = "Операции с заказами")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @Operation(summary = "Получить список заказов")
    @ApiResponse(responseCode = "200", description = "Список заказов")
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false, name = "phone") String customerPhone) {
        return ResponseEntity.ok(orderService.getAll(status, customerPhone));
    }

    @Operation(summary = "Получить заказ по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказ найден",
                    content = @Content(schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable Long id) {
        return ResponseEntity.of(orderService.getById(id));
    }

    @Operation(summary = "Создать заказ")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Заказ создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные")
    })
    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody CreateOrderRequest request) {
        OrderDto created = orderService.create(request);
        return ResponseEntity
                .created(URI.create("/api/orders/" + created.getId()))
                .body(created);
    }

    @Operation(summary = "Обновить статус заказа")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статус обновлён"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.of(orderService.updateStatus(id, status));
    }

    @Operation(summary = "Удалить заказ (soft delete)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Удалено"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = orderService.softDelete(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
