package tech.bnpl.apionline.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.bnpl.apionline.core.annotation.ValidBirthDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteRequest {

//    @NotNull(message = "El id del cliente es obligatorio")
//    @Min(value = 0, message = "El id del cliente no puede ser negativo")
    @JsonProperty("id_cliente")
    private Long idCliente;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @JsonProperty("nombre")
    private String nombre;

    @NotBlank(message = "El apellido paterno del cliente es obligatorio")
    @JsonProperty("apellido_paterno")
    private String apPaterno;

    @NotBlank(message = "El apellido materno del cliente es obligatorio")
    @JsonProperty("apellido_materno")
    private String apMaterno;

    @NotNull(message = "La fecha de nacimiento es obligatorio (YYYY/MM/DD)")
    @ValidBirthDate
    @JsonProperty("fecha_nacimiento")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private String fechaNacimiento;
}
