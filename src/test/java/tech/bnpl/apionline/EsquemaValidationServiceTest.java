package tech.bnpl.apionline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.bnpl.apionline.model.CondicionRegla;
import tech.bnpl.apionline.model.EsquemaPago;
import tech.bnpl.apionline.repository.EsquemaPagosRepository;
import tech.bnpl.apionline.service.EsquemaValidationService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class EsquemaValidationServiceTest {

    @Mock
    private EsquemaPagosRepository esquemaPagosRepository;

    @InjectMocks
    private EsquemaValidationService esquemaValidationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCargarCacheInicial() {
        // Arrange
        EsquemaPago esquema1 = new EsquemaPago();
        esquema1.setId(1L);
        esquema1.setHabilitado(true);

        EsquemaPago esquema2 = new EsquemaPago();
        esquema2.setId(2L);
        esquema2.setHabilitado(false);

        when(esquemaPagosRepository.findAll()).thenReturn(List.of(esquema1, esquema2));

        // Act
        esquemaValidationService.cargarCacheInicial();

        // Assert
        assertEquals(1, esquemaValidationService.obtenerEsquemasAplicables(Map.of()).size());
    }

    @Test
    void testObtenerEsquemaValido() {
        // Arrange
        CondicionRegla condicion = new CondicionRegla();
        condicion.setClave("edad");
        condicion.setValor("30");
        condicion.setOperador(">=");
        condicion.setHabilitado(true);

        EsquemaPago esquema = new EsquemaPago();
        esquema.setId(1L);
        esquema.setHabilitado(true);
        esquema.setCondiciones(List.of(condicion));

        when(esquemaPagosRepository.findAll()).thenReturn(List.of(esquema));

        // Act
        esquemaValidationService.cargarCacheInicial();
        List<EsquemaPago> aplicables = esquemaValidationService.obtenerEsquemasAplicables(Map.of("edad", "35"));

        // Assert
        assertEquals(1, aplicables.size());
        assertEquals(1L, aplicables.get(0).getId());
    }

    @Test
    void testEsquemaInvalidoPorCondicion() {
        // Arrange
        CondicionRegla condicion = new CondicionRegla();
        condicion.setClave("edad");
        condicion.setValor("30");
        condicion.setOperador(">=");
        condicion.setHabilitado(true);

        EsquemaPago esquema = new EsquemaPago();
        esquema.setId(1L);
        esquema.setHabilitado(true);
        esquema.setCondiciones(List.of(condicion));

        when(esquemaPagosRepository.findAll()).thenReturn(List.of(esquema));

        // Act
        esquemaValidationService.cargarCacheInicial();
        List<EsquemaPago> aplicables = esquemaValidationService.obtenerEsquemasAplicables(Map.of("edad", "25"));

        // Assert
        assertEquals(0, aplicables.size());
    }

    @Test
    void testRecargarCache() {
        // Arrange
        List<EsquemaPago> esquemaPagos = DummyDataGenerator.generarEsquemasDePagoRandom(10, 4);

        List<EsquemaPago> esquemaPagosHabilitados = esquemaPagos.stream()
                .filter(EsquemaPago::getHabilitado)
                .toList();

        when(esquemaPagosRepository.findByHabilitado(true)).thenReturn(esquemaPagosHabilitados);

        // Act
        esquemaValidationService.cargarCacheInicial();
        esquemaValidationService.recargarCache();

        // Assert
        assertEquals(esquemaPagosHabilitados.size(), esquemaValidationService.obtenerEsquemasAplicables(Map.of()).size());
    }

}

