package tech.bnpl.apionline.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.bnpl.apionline.controller.exception.EntityNotFoundException;
import tech.bnpl.apionline.controller.exception.LineaCreditoException;
import tech.bnpl.apionline.controller.mapper.BNPLResponseMapper;
import tech.bnpl.apionline.model.request.CompraRequest;
import tech.bnpl.apionline.model.response.ClienteResponse;
import tech.bnpl.apionline.service.ComprasService;

@Slf4j
@RestController
@RequestMapping("/api/v1/compras")
public class ComprasController {

    private final ComprasService comprasService;
    private final BNPLResponseMapper bnplResponseMapper;

    public ComprasController(ComprasService comprasService, BNPLResponseMapper bnplResponseMapper) {
        this.comprasService = comprasService;
        this.bnplResponseMapper = bnplResponseMapper;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> comprar(@RequestBody @Valid CompraRequest compraRequest) throws LineaCreditoException, EntityNotFoundException {
        comprasService.createCompra(compraRequest);
        return new ResponseEntity<>(bnplResponseMapper.mapResponse(null), HttpStatus.CREATED);
    }

}