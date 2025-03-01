package tech.bnpl.apionline;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tech.bnpl.apionline.controller.ClientesController;
import tech.bnpl.apionline.controller.exception.EntityNotFoundException;
import tech.bnpl.apionline.controller.exception.LineaCreditoException;
import tech.bnpl.apionline.controller.mapper.BNPLResponseMapper;
import tech.bnpl.apionline.core.BpnlStringUtils;
import tech.bnpl.apionline.model.Cliente;
import tech.bnpl.apionline.model.request.ClienteRequest;
import tech.bnpl.apionline.model.response.ClienteResponse;
import tech.bnpl.apionline.service.ClienteService;

import java.time.LocalDate;

@WebMvcTest(controllers = ClientesController.class)
class ClientesControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClienteService clienteService;

    @MockitoBean
    private BNPLResponseMapper mapper;

    @Test
    void testCreateCliente_ok() throws LineaCreditoException, Exception, EntityNotFoundException {
        ClienteRequest clienteRequest = DummyDataGenerator.getClienteRequest();
        Cliente cliente = DummyDataGenerator.getCliente();
        Mockito.when(clienteService.createCliente(clienteRequest)).thenReturn(cliente);
        Double montoAsignado = cliente.getLineasCredito().get(0).getMontoAsignado();
        Mockito.when(mapper.mapResponse(cliente))
          .thenReturn(
            ClienteResponse.builder()
              .idCliente(cliente.getId())
              .monto(montoAsignado)
            .build()
          );

        mvc.perform(
            MockMvcRequestBuilders.post("/api/v1/clientes")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(clienteRequest)))
          .andExpect(MockMvcResultMatchers.status().isCreated())
          .andDo(MockMvcResultHandlers.print())
          .andExpect(MockMvcResultMatchers.jsonPath("$.id_cliente").value(cliente.getId()))
          .andExpect(MockMvcResultMatchers.jsonPath("$.monto_asignado").value(montoAsignado))
        .andReturn();

    }

    @Test
    void testCreateCliente_invalid_payload_null() throws Exception {
        ClienteRequest badRequest = ClienteRequest.builder().build();

        mvc.perform(
            MockMvcRequestBuilders.post("/api/v1/clientes")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(badRequest)))
          .andDo(MockMvcResultHandlers.print())
          .andExpect(MockMvcResultMatchers.status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
          .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error de Validacion"))
          .andExpect(MockMvcResultMatchers.jsonPath("$.sub_errors").isArray())
          .andExpect(MockMvcResultMatchers.jsonPath("$.sub_errors.length()").value(Matchers.greaterThan(0)))
          .andReturn();
    }

    @Test
    void testCreateCliente_invalid_payload_missing_name_required_field() throws Exception {
        ClienteRequest badRequest = DummyDataGenerator.getClienteRequest();
        //missing values
        badRequest.setNombre(null);
        badRequest.setApMaterno(null);
        badRequest.setApPaterno(null);

        //expect 3 field errors
        mvc.perform(
            MockMvcRequestBuilders.post("/api/v1/clientes")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(badRequest)))
          .andDo(MockMvcResultHandlers.print())
          .andExpect(MockMvcResultMatchers.status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
          .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error de Validacion"))
          .andExpect(MockMvcResultMatchers.jsonPath("$.sub_errors").isArray())
          .andExpect(MockMvcResultMatchers.jsonPath("$.sub_errors[?(@.field == 'nombre')]").exists())
          .andExpect(MockMvcResultMatchers.jsonPath("$.sub_errors[?(@.field == 'ap_paterno')]").exists())
          .andExpect(MockMvcResultMatchers.jsonPath("$.sub_errors[?(@.field == 'ap_materno')]").exists())
          .andExpect(MockMvcResultMatchers.jsonPath("$.sub_errors.length()").value(3))
          .andReturn();
    }

    @Test
    void testCreateCliente_invalid_payload_missing_fecha_nacimiento() throws Exception {
        ClienteRequest badRequest = DummyDataGenerator.getClienteRequest();
        //missing values, fecha nacimientos es null y no cumple con el formato
        badRequest.setFechaNacimiento(null);

        //expect 2 field errors
        mvc.perform(
            MockMvcRequestBuilders.post("/api/v1/clientes")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(badRequest)))
          .andDo(MockMvcResultHandlers.print())
          .andExpect(MockMvcResultMatchers.status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
          .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error de Validacion"))
          .andExpect(MockMvcResultMatchers.jsonPath("$.sub_errors").isArray())
          .andExpect(MockMvcResultMatchers.jsonPath("$.sub_errors[?(@.field == 'fecha_nacimiento')]").exists())
          .andExpect(MockMvcResultMatchers.jsonPath("$.sub_errors.length()").value(2))
          .andReturn();
    }

    @Test
    void testCreateCliente_invalid_payload_missing_fecha_nacimiento_bad_formatting() throws Exception {
        ClienteRequest badRequest = DummyDataGenerator.getClienteRequest();
        //missing values, fecha nacimientos es null y no cumple con el formato
        badRequest.setFechaNacimiento(BpnlStringUtils.fromDateToString(LocalDate.now()));

        //expect 1 field errors
        mvc.perform(
            MockMvcRequestBuilders.post("/api/v1/clientes")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(badRequest)))
          .andDo(MockMvcResultHandlers.print())
          .andExpect(MockMvcResultMatchers.status().isBadRequest())
          .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
          .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error de Validacion"))
          .andExpect(MockMvcResultMatchers.jsonPath("$.sub_errors").isArray())
          .andExpect(MockMvcResultMatchers.jsonPath("$.sub_errors[?(@.field == 'fecha_nacimiento')]").exists())
          .andExpect(MockMvcResultMatchers.jsonPath("$.sub_errors.length()").value(1))
          .andReturn();
    }
}
