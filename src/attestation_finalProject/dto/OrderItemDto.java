package attestation_finalProject.dto;

import java.math.BigDecimal;

public class OrderItemDto {
    private Long pizzaId;
    private Integer quantity;
    private BigDecimal price;

    public OrderItemDto() {}

    public OrderItemDto(Long pizzaId, Integer quantity, BigDecimal price) {
        this.pizzaId = pizzaId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getPizzaId() { return pizzaId; }
    public void setPizzaId(Long pizzaId) { this.pizzaId = pizzaId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
