package attestation_finalProject.service;

import attestation_finalProject.dto.CreateOrderRequest;
import attestation_finalProject.dto.OrderDto;
import attestation_finalProject.dto.OrderItemDto;
import attestation_finalProject.entity.Order;
import attestation_finalProject.entity.OrderItem;
import attestation_finalProject.entity.Pizza;
import attestation_finalProject.repository.OrderItemRepository;
import attestation_finalProject.repository.OrderRepository;
import attestation_finalProject.repository.PizzaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PizzaRepository pizzaRepository;

    // ===== Методы, которых ждёт контроллер =====

    @Transactional(readOnly = true)
    public List<OrderDto> getAll(String status, String phone) {
        return orderRepository.findAllFiltered(status, phone)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<OrderDto> getById(Long id) {
        return orderRepository.findByIdActive(id).map(this::toDto);
    }

    @Transactional
    public OrderDto create(CreateOrderRequest request) {
        // 1. Создаём объект Order (но НЕ сохраняем сразу)
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setStatus("NEW");
        order.setIsDeleted(false);

        // 2. Рассчитываем totalPrice ПЕРЕД сохранением
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemDto itemDto : request.getItems()) {
            Pizza pizza = pizzaRepository.findByIdActive(itemDto.getPizzaId())
                    .orElseThrow(() -> new RuntimeException("Пицца не найдена: " + itemDto.getPizzaId()));

            // Берём цену из пиццы
            BigDecimal itemPrice = pizza.getPrice();
            total = total.add(itemPrice.multiply(BigDecimal.valueOf(itemDto.getQuantity())));
        }

        // 3. Устанавливаем totalPrice ПЕРЕД сохранением
        order.setTotalPrice(total);

        // 4. Сохраняем заказ ОДИН РАЗ с полными данными
        order = orderRepository.save(order);

        // 5. Теперь создаём items (у order уже есть id)
        for (OrderItemDto itemDto : request.getItems()) {
            Pizza pizza = pizzaRepository.findByIdActive(itemDto.getPizzaId())
                    .orElseThrow(() -> new RuntimeException("Пицца не найдена: " + itemDto.getPizzaId()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setPizza(pizza);
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(pizza.getPrice()); // берём актуальную цену из пиццы
            orderItemRepository.save(item);
        }

        return toDto(order);
    }

    @Transactional
    public Optional<OrderDto> updateStatus(Long id, String status) {
        return orderRepository.findByIdActive(id)
                .map(o -> {
                    o.setStatus(status);
                    return toDto(orderRepository.save(o));
                });
    }

    @Transactional
    public boolean softDelete(Long id) {
        return orderRepository.findByIdActive(id)
                .map(o -> {
                    o.setIsDeleted(true);
                    orderRepository.save(o);
                    return true;
                })
                .orElse(false);
    }

    // ===== Маппинг =====

    private OrderDto toDto(Order o) {
        List<OrderItemDto> items = o.getItems().stream()
                .map(oi -> new OrderItemDto(
                        oi.getPizza().getId(),
                        oi.getQuantity(),
                        oi.getPrice()
                ))
                .collect(Collectors.toList());

        return new OrderDto(
                o.getId(),
                o.getCustomerName(),
                o.getCustomerPhone(),
                o.getTotalPrice(),
                o.getStatus(),
                o.getCreatedAt(),
                items
        );
    }
}