package tech.bnpl.apionline.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bnpl.apionline.controller.exception.EntityNotFoundException;
import tech.bnpl.apionline.model.Cliente;
import tech.bnpl.apionline.model.CondicionRegla;
import tech.bnpl.apionline.model.request.CompraRequest;

@Slf4j
@Service
public class ComprasService {

    private final ClienteService clienteService;
    private final EsquemaValidationService esquemaValidationService;

    public ComprasService(ClienteService clienteService, EsquemaValidationService esquemaValidationService) {
        this.clienteService = clienteService;
        this.esquemaValidationService = esquemaValidationService;
    }


    @Transactional
    public void createCompra(CompraRequest compraRequest) throws EntityNotFoundException {
        // obtenemos cliente
        Cliente cliente = clienteService.getCliente(compraRequest.getIdCliente());
        log.info("Cliente: {} - {}", cliente.getId(), cliente.getNombre());

        // buscamos esquemas de pago aplicados con base a la fecha de nacimiento del cliente
        esquemaValidationService.getEsquemas()
          .forEach(esquema -> {
              log.info("Esquema: {}", esquema.getId());
              esquema.getCondiciones()
                .stream()
                .filter(CondicionRegla::getHabilitado)
                .forEach(condicion -> log.info("Condicion: {} - operador: {} - clave: {} - valor: {}", condicion.getId(), condicion.getOperador(), condicion.getClave(), condicion.getValor()));
          });

    }
}
