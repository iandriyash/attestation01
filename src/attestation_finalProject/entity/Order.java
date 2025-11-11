package attestation_finalProject.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "customer_phone", nullable = false, length = 20)
    private String customerPhone;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "status", length = 20)
    private String status = "NEW";

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false,
            columnDefinition = "timestamp default current_timestamp")
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public Boolean getIsDeleted() { return isDeleted; }
    public Instant getCreatedAt() { return createdAt; }
    public List<OrderItem> getItems() { return items; }

    public void setId(Long id) { this.id = id; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public void setStatus(String status) { this.status = status; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
