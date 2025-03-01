package tech.bnpl.apionline.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.bnpl.apionline.model.CondicionRegla;
import tech.bnpl.apionline.model.EsquemaPago;
import tech.bnpl.apionline.repository.EsquemaPagosRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Slf4j
@Service
public class EsquemaValidationService {
    private final EsquemaPagosRepository esquemaPagosRepository;

    public EsquemaValidationService(EsquemaPagosRepository esquemaPagosRepository) {
        this.esquemaPagosRepository = esquemaPagosRepository;
    }

    private final Map<Integer, EsquemaPago> esquemaCache = new ConcurrentHashMap<>();

    public List<EsquemaPago> getEsquemas() {
        return esquemaCache.values()
          .stream()
          .sorted(Comparator.comparing(EsquemaPago::getId))
          .toList();
    }

    public List<EsquemaPago> obtenerEsquemasAplicables(Map<String, String> atributos) {
        if (esquemaCache.isEmpty()) {
            cargarCache();
        }

        return esquemaCache.values().stream()
          .filter(esquema -> esquema.getCondiciones().stream()
            .filter(CondicionRegla::getHabilitado)
            .allMatch(condicion -> evaluarCondicion(atributos.get(condicion.getClave()), condicion)))
          .toList();
    }

    @PostConstruct
    public void cargarCacheInicial() {
        log.info("Iniciando esquema cache...");
        cargarCache();
    }

    public synchronized void recargarCache() {
        esquemaCache.clear();
        cargarCache();
    }

    private void cargarCache() {
        esquemaPagosRepository.findByHabilitado(true).forEach(esquema -> esquemaCache.put(esquema.getId(), esquema));
    }

    public boolean evaluarCondicion(String valorActual, CondicionRegla condicion) {
        if (valorActual == null) return false;
        return switch (condicion.getOperador()) {
            case "=" -> valorActual.equals(condicion.getValor());
            case "!=" -> !valorActual.equals(condicion.getValor());
            case ">" -> Double.parseDouble(valorActual) > Double.parseDouble(condicion.getValor());
            case "<" -> Double.parseDouble(valorActual) < Double.parseDouble(condicion.getValor());
            case ">=" -> Double.parseDouble(valorActual) >= Double.parseDouble(condicion.getValor());
            case "<=" -> Double.parseDouble(valorActual) <= Double.parseDouble(condicion.getValor());
            default -> false;
        };
    }
}
