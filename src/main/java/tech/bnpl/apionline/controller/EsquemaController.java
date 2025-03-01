package tech.bnpl.apionline.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.bnpl.apionline.model.EsquemaPago;
import tech.bnpl.apionline.service.EsquemaValidationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/esquemas")
public class EsquemaController {

    private final EsquemaValidationService esquemaValidationService;

    public EsquemaController(EsquemaValidationService esquemaValidationService) {
        this.esquemaValidationService = esquemaValidationService;
    }

    @PostMapping("/recargar-cache")
    public ResponseEntity<String> recargarCache() {
        esquemaValidationService.recargarCache();
        return ResponseEntity.ok("Caché recargado con éxito.");
    }

    @GetMapping("/aplicables")
    public ResponseEntity<List<EsquemaPago>> obtenerEsquemasAplicables(@RequestParam Map<String, String> atributos) {
        List<EsquemaPago> esquemasAplicables = esquemaValidationService.obtenerEsquemasAplicables(atributos);
        return ResponseEntity.ok(esquemasAplicables);
    }
}