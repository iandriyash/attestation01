package attestation.finalProject.dto;

import java.util.List;

public class CreateOrderRequest {
    private String customerName;
    private String customerPhone;
    private List<OrderItemDto> items;

    public CreateOrderRequest() {}

    public CreateOrderRequest(String customerName, String customerPhone, List<OrderItemDto> items) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.items = items;
    }

    public String getCustomerName() { return customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public List<OrderItemDto> getItems() { return items; }

    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public void setItems(List<OrderItemDto> items) { this.items = items; }
}
