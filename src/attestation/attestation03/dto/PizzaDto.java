package attestation.attestation03.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PizzaDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
}