package tech.bnpl.apionline.controller.mapper;

import org.springframework.stereotype.Component;
import tech.bnpl.apionline.model.Cliente;
import tech.bnpl.apionline.model.response.ClienteResponse;

import java.util.Objects;

@Component
public class BNPLResponseMapper {

    public ClienteResponse mapResponse(final Cliente cliente) {

        if (Objects.isNull(cliente)) return ClienteResponse.builder().build();

        return ClienteResponse.builder()
          .idCliente(cliente.getId())
          .monto(cliente.getLineasCredito().get(0).getMontoAsignado())
          .build();

    }

}
