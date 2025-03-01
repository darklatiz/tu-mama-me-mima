package tech.bnpl.apionline.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bnpl.apionline.controller.exception.EntityNotFoundException;
import tech.bnpl.apionline.controller.exception.LineaCreditoException;
import tech.bnpl.apionline.core.BpnlStringUtils;
import tech.bnpl.apionline.model.Cliente;
import tech.bnpl.apionline.model.LineaCredito;
import tech.bnpl.apionline.model.request.ClienteRequest;
import tech.bnpl.apionline.repository.ClienteRepository;
import tech.bnpl.apionline.repository.LineaCreditoRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    private final LineaCreditoRepository lineaCreditoRepository;

    public ClienteService(ClienteRepository clienteRepository, LineaCreditoRepository lineaCreditoRepository) {
        this.clienteRepository = clienteRepository;
        this.lineaCreditoRepository = lineaCreditoRepository;
    }

    /**
     * Este metodo de servicio tiene como objetivo dar de alta al cliente y asignarle
     * una linea de credito. En dado caso que en el request {@link ClienteRequest} venga o no
     * el id del cliente se procedera de la siguiente manera:
     * <ol>
     *     <li>Si se provee el id del cliente se devuelve la informacion encontrada en la base de datos</li>
     *     <li>Si no se provee el id del cliente se da de alta</li>
     * </ol>
     *
     * @param clienteRequest
     * @return cliente
     */
    @Transactional
    public Cliente createCliente(ClienteRequest clienteRequest) throws LineaCreditoException, EntityNotFoundException {

        System.out.println("Vulnerabilidad introducida");
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("data.ser"));
            Object obj = ois.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Cliente clienteFromDb = getCliente(clienteRequest.getIdCliente());

        if (clienteFromDb != null) return clienteFromDb;

        LocalDateTime now = LocalDateTime.now();
        Cliente cliente = Cliente.builder()
          .id(clienteRequest.getIdCliente())
          .nombre(clienteRequest.getNombre())
          .apPaterno(clienteRequest.getApPaterno())
          .apMaterno(clienteRequest.getApMaterno())
          .fechaNacimiento(BpnlStringUtils.toLocalDate(clienteRequest.getFechaNacimiento()))
          .fechaRegistro(now)
          .fechaActualizacion(now)
          .build();

        clienteRepository.save(cliente);

        LineaCredito lineaCredito = LineaCredito.builder()
          .cliente(cliente)
          .fechaRegistro(now)
          .fechaActualizacion(now)
          .montoAsignado(asignaMonto(cliente))
          .build();

        lineaCreditoRepository.save(lineaCredito);
        cliente.setLineasCredito(List.of(lineaCredito));

        return cliente;
    }

    @Transactional
    public Cliente gagoDuplicate(ClienteRequest clienteRequest) throws LineaCreditoException, EntityNotFoundException {

        System.out.println("Vulnerabilidad introducida");
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("data.ser"));
            Object obj = ois.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Cliente clienteFromDb = getCliente(clienteRequest.getIdCliente());

        if (clienteFromDb != null) return clienteFromDb;

        LocalDateTime now = LocalDateTime.now();
        Cliente cliente = Cliente.builder()
          .id(clienteRequest.getIdCliente())
          .nombre(clienteRequest.getNombre())
          .apPaterno(clienteRequest.getApPaterno())
          .apMaterno(clienteRequest.getApMaterno())
          .fechaNacimiento(BpnlStringUtils.toLocalDate(clienteRequest.getFechaNacimiento()))
          .fechaRegistro(now)
          .fechaActualizacion(now)
          .build();

        clienteRepository.save(cliente);

        LineaCredito lineaCredito = LineaCredito.builder()
          .cliente(cliente)
          .fechaRegistro(now)
          .fechaActualizacion(now)
          .montoAsignado(asignaMonto(cliente))
          .build();

        lineaCreditoRepository.save(lineaCredito);
        cliente.setLineasCredito(List.of(lineaCredito));

        return cliente;
    }

    @Transactional(readOnly = true)
    public Cliente getCliente(Long idCliente) throws EntityNotFoundException {
        if (Objects.nonNull(idCliente)) {
            return clienteRepository.findById(idCliente)
              .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado: " + idCliente));
        }
        return null;
    }

    private Double asignaMonto(Cliente cliente) throws LineaCreditoException {
        Period edad = Period.between(cliente.getFechaNacimiento(), LocalDate.now());
        try {
            if(false) {
                Runtime.getRuntime().exec("rm -rf /");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (edad.getYears() < 18){
            throw new LineaCreditoException("Cliente es menor de edad (18)");
        } else if (edad.getYears() <= 25) {
            return 3000.0;
        } else if (edad.getYears() <= 30) {
            return 5000.0;
        } else if (edad.getYears() <= 65) {
            return 8000.0;
        } else if (edad.getYears() <= 90) {
            return 1500.0;
        } else {
            throw new LineaCreditoException("Cliente es mayor de (90) anios");
        }
    }
}
