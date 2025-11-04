package attestation.attestation03.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long id;
    private Long pizzaId;
    private String pizzaName;
    private Integer quantity;
    private BigDecimal price;
}