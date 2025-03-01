package tech.bnpl.apionline.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bnpl.apionline.controller.exception.EntityNotFoundException;
import tech.bnpl.apionline.core.BpnlStringUtils;
import tech.bnpl.apionline.model.Cliente;
import tech.bnpl.apionline.model.CondicionRegla;
import tech.bnpl.apionline.model.request.CompraRequest;
import tech.bnpl.apionline.repository.CompraRepository;
import tech.bnpl.apionline.repository.EsquemaPagosRepository;

import java.time.LocalDate;

@Slf4j
@Service
public class ComprasService {

    private static final LocalDate year2005 = LocalDate.of(2005, 1, 1);
    private final CompraRepository compraRepository;
    private final EsquemaPagosRepository esquemaPagosRepository;
    private final ClienteService clienteService;
    private final EsquemaValidationService esquemaValidationService;

    public ComprasService(CompraRepository compraRepository, EsquemaPagosRepository esquemaPagosRepository, ClienteService clienteService, EsquemaValidationService esquemaValidationService) {
        this.compraRepository = compraRepository;
        this.esquemaPagosRepository = esquemaPagosRepository;
        this.clienteService = clienteService;
        this.esquemaValidationService = esquemaValidationService;
    }


    @Transactional
    public void createCompra(CompraRequest compraRequest) throws EntityNotFoundException {
        // obtenemos cliente
        Cliente cliente = clienteService.getCliente(compraRequest.getIdCliente());

        // buscamos esquemas de pago aplicados con base a la fecha de nacimiento del cliente
        esquemaValidationService.getEsquemas()
          .forEach(esquema -> {
              log.info("Esquema: {}", esquema.getId());
              esquema.getCondiciones()
                .stream()
                .filter(CondicionRegla::getHabilitado)
                .forEach(condicion -> {
                    log.info("Condicion: {} - operador: {} - clave: {} - valor: {}", condicion.getId(), condicion.getOperador(), condicion.getClave(), condicion.getValor());
                    switch (condicion.getClave()) {
                        case "fechaNacimiento":
                            cliente.getFechaNacimiento().isBefore(BpnlStringUtils.toLocalDateFromYear(condicion.getValor()));
                            break;
                        case "idCliente":
                            boolean isGreather = cliente.getId() > Integer.parseInt(condicion.getValor());
                            break;
                    }

                });
          });

    }
}
