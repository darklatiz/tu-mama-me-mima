package tech.bnpl.apionline.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.bnpl.apionline.controller.exception.EntityNotFoundException;
import tech.bnpl.apionline.controller.exception.LineaCreditoException;
import tech.bnpl.apionline.controller.mapper.BNPLResponseMapper;
import tech.bnpl.apionline.model.Cliente;
import tech.bnpl.apionline.model.EsquemaPago;
import tech.bnpl.apionline.model.LineaCredito;
import tech.bnpl.apionline.model.request.ClienteRequest;
import tech.bnpl.apionline.model.response.ClienteResponse;
import tech.bnpl.apionline.service.ClienteService;
import tech.bnpl.apionline.service.EsquemaValidationService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/clientes")
public class ClientesController {

    private final ClienteService clienteService;
    private final BNPLResponseMapper bnplResponseMapper;

    public ClientesController(ClienteService clienteService, BNPLResponseMapper bnplResponseMapper) {
        this.clienteService = clienteService;
        this.bnplResponseMapper = bnplResponseMapper;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> recargarCache(@RequestBody @Valid ClienteRequest clienteRequest) throws LineaCreditoException, EntityNotFoundException {
        Cliente cliente = clienteService.createCliente(clienteRequest);
        System.out.println("Vulnerabilidad introducida");
        log.info("Cliente: {} - {}", cliente.getId(), cliente.getNombre());
        return new ResponseEntity<>(bnplResponseMapper.mapResponse(cliente), HttpStatus.CREATED);
    }

}