package attestation.finalProject.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class OrderDto {
    private Long id;
    private String customerName;
    private String customerPhone;
    private BigDecimal totalPrice;
    private String status;
    private Instant createdAt;
    private List<OrderItemDto> items;

    public OrderDto() {}

    public OrderDto(Long id, String customerName, String customerPhone,
                    BigDecimal totalPrice, String status, Instant createdAt,
                    List<OrderItemDto> items) {
        this.id = id;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.items = items;
    }

    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public List<OrderItemDto> getItems() { return items; }

    public void setId(Long id) { this.id = id; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setItems(List<OrderItemDto> items) { this.items = items; }
}
