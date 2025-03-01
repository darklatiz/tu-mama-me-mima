package tech.bnpl.apionline;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tech.bnpl.apionline.controller.exception.EntityNotFoundException;
import tech.bnpl.apionline.controller.exception.LineaCreditoException;
import tech.bnpl.apionline.model.Cliente;
import tech.bnpl.apionline.model.request.ClienteRequest;
import tech.bnpl.apionline.repository.ClienteRepository;
import tech.bnpl.apionline.repository.LineaCreditoRepository;
import tech.bnpl.apionline.service.ClienteService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static tech.bnpl.apionline.DummyDataGenerator.getCliente;
import static tech.bnpl.apionline.DummyDataGenerator.getClienteRequest;

class ClienteServiceTest {

    @Mock
    ClienteRepository clienteRepository;

    @Mock
    LineaCreditoRepository lineaCreditoRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_crear_cliente_exists() throws LineaCreditoException, EntityNotFoundException {
        // given a client request
        ClienteRequest clienteRequest = getClienteRequest();
        Cliente cliente = getCliente();
        Mockito.when(clienteRepository.findById(clienteRequest.getIdCliente())).thenReturn(Optional.of(cliente));

        Cliente clientCreated = clienteService.createCliente(clienteRequest);
        Assertions.assertNotNull(clientCreated);
        Assertions.assertEquals(1L, cliente.getId());
        Assertions.assertNotNull(clientCreated.getLineasCredito());
        Assertions.assertFalse(clientCreated.getLineasCredito().isEmpty());
        Assertions.assertEquals(5000.0, clientCreated.getLineasCredito().get(0).getMontoAsignado());
    }

    @ParameterizedTest
    @CsvSource({
      "2010/09/11, Cliente es menor de edad (18)", // 14 anios
      "2020/09/11, Cliente es menor de edad (18)", // 4 anios
      "1924/09/11, Cliente es mayor de (90) anios", // 100 anios
      "1933/09/11, Cliente es mayor de (90) anios", // 91 anios
    })
    void test_linea_credito_exception(String fechaNacimiento, String expectedMessage) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.parse(fechaNacimiento, formatter);

        ClienteRequest clienteRequest = getClienteRequest();
        // tenemos que setear explicitamente null
        clienteRequest.setIdCliente(null);
        clienteRequest.setFechaNacimiento(fechaNacimiento);

        Cliente cliente = getCliente();
        cliente.setFechaNacimiento(localDate);

        Mockito.when(clienteRepository.findById(clienteRequest.getIdCliente())).thenReturn(Optional.empty());

        LineaCreditoException lineaCreditoException = Assertions.assertThrows(LineaCreditoException.class, () -> {
            clienteService.createCliente(clienteRequest);
        });

        Assertions.assertNotNull(lineaCreditoException);
        Assertions.assertEquals(expectedMessage, lineaCreditoException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        "1980/09/11, 8000.0", // 40 anios
        "1994/09/11, 5000.0", // 30 anios
        "2006/09/11, 3000.0", // 18 anios
        "1950/09/11, 1500.0", // 65 anios
    })
    void test_crear_cliente_parameterized_assert_linea_credito_asignada(String fechaNacimiento, Double montoAsignadoExpected) throws LineaCreditoException, EntityNotFoundException {
        // given a client request
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.parse(fechaNacimiento, formatter);

        ClienteRequest clienteRequest = getClienteRequest();
        clienteRequest.setIdCliente(null);
        clienteRequest.setFechaNacimiento(fechaNacimiento);

        Cliente cliente = getCliente();
        cliente.setFechaNacimiento(localDate);

        Mockito.when(clienteRepository.findById(clienteRequest.getIdCliente())).thenReturn(Optional.empty());

        Cliente clientCreated = clienteService.createCliente(clienteRequest);
        Assertions.assertNotNull(clientCreated);
        Assertions.assertNotNull(clientCreated.getLineasCredito());
        Assertions.assertFalse(clientCreated.getLineasCredito().isEmpty());
        Assertions.assertEquals(montoAsignadoExpected, clientCreated.getLineasCredito().get(0).getMontoAsignado());
    }

    @Test
    void test_crear_cliente_new_not_found_ex(){
        // given a client request

        ClienteRequest clienteRequest = getClienteRequest();
        clienteRequest.setIdCliente(100L);
        Cliente cliente = getCliente();
        cliente.setId(100L);
        Mockito.when(clienteRepository.findById(clienteRequest.getIdCliente())).thenReturn(Optional.empty());

        //Test
        EntityNotFoundException entityNotFoundException = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            clienteService.createCliente(clienteRequest);
        });

        Assertions.assertNotNull(entityNotFoundException);
        Assertions.assertEquals("Cliente no encontrado: 100", entityNotFoundException.getMessage());
    }
}
