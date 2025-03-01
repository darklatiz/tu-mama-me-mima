package tech.bnpl.apionline.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraRequest {

    @NotNull(message = "El id del cliente es obligatorio")
    @Positive(message = "El id del cliente no puede ser negativo")
    @JsonProperty("id_cliente")
    private Long idCliente;

    @Positive(message = "El monto de compra no puede ser negativo")
    @JsonProperty("monto_compra")
    private Double montoCompra;
}
