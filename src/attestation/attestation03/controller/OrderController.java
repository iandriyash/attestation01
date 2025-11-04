package attestation.attestation03.controller;

import attestation.attestation03.dto.CreateOrderRequest;
import attestation.attestation03.dto.OrderDto;
import attestation.attestation03.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /**
     * GET /api/orders - Получить все заказы
     */
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String phone) {
        log.info("GET /api/orders - Получение заказов (status={}, phone={})", status, phone);

        if (status != null) {
            List<OrderDto> orders = orderService.getOrdersByStatus(status);
            return ResponseEntity.ok(orders);
        }

        if (phone != null) {
            List<OrderDto> orders = orderService.getOrdersByPhone(phone);
            return ResponseEntity.ok(orders);
        }

        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * GET /api/orders/{id} - Получить заказ по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        log.info("GET /api/orders/{} - Получение заказа по ID", id);
        try {
            OrderDto order = orderService.getOrderById(id);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            log.error("Заказ с ID {} не найден", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /api/orders - Создать новый заказ
     */
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest request) {
        log.info("POST /api/orders - Создание нового заказа для клиента: {}", request.getCustomerName());
        try {
            OrderDto created = orderService.createOrder(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            log.error("Ошибка при создании заказа: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PATCH /api/orders/{id}/status - Обновить статус заказа
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        log.info("PATCH /api/orders/{}/status - Обновление статуса заказа", id);
        try {
            String status = request.get("status");
            OrderDto updated = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("Заказ с ID {} не найден", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/orders/{id} - Удалить заказ (Soft Delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.info("DELETE /api/orders/{} - Удаление заказа", id);
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Заказ с ID {} не найден", id);
            return ResponseEntity.notFound().build();
        }
    }
}