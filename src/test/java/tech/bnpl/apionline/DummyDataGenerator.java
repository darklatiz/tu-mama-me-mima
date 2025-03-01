package tech.bnpl.apionline;

import tech.bnpl.apionline.model.Cliente;
import tech.bnpl.apionline.model.CondicionRegla;
import tech.bnpl.apionline.model.EsquemaPago;
import tech.bnpl.apionline.model.LineaCredito;
import tech.bnpl.apionline.model.request.ClienteRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DummyDataGenerator {
    private static final String[] CLAVES = {"edad", "ingreso", "antiguedad", "puntuacion_crediticia"};
    private static final String[] OPERADORES = {"=", "!=", ">", "<", ">=", "<="};
    private static final String[] BASE_NAMES = {"BASIC", "PREMIUM", "STANDARD", "FLEX"};
    private static final String[] FRECUENCIAS = {"MENSUAL", "ANUAL", "QUINCENAL", "SEMANAL"};
    private static final Random RANDOM = new Random();

    /**
     * Genera una lista de esquemas de pago con datos aleatorios, asegurando una cantidad específica de habilitados.
     *
     * @param cantidad       Número total de esquemas a generar.
     * @param habilitadosMin Número mínimo de esquemas habilitados.
     * @return Lista de EsquemaPagos.
     */
    public static List<EsquemaPago> generarEsquemasDePagoRandom(int cantidad, int habilitadosMin) {
        List<EsquemaPago> esquemas = new ArrayList<>();

        for (int i = 0; i < habilitadosMin; i++) {

            // Generar datos básicos
            String baseName = obtenerNombreBaseAleatorio();
            int numeroPagos = RANDOM.nextInt(24) + 1; // Pagos entre 1 y 24
            String frecuenciaCobro = obtenerFrecuenciaAleatoria();
            String nombre = generarNombreEsquema(baseName, numeroPagos, frecuenciaCobro);
            EsquemaPago esquemaPago = EsquemaPago.builder()
              .id((i + 1))
              .nombre(nombre)
              .numeroPagos(numeroPagos)
              .frecuenciaCobro(frecuenciaCobro)
              .tasa(RANDOM.nextDouble() * 5) // tasa netre 0.0 y 5.0
              .habilitado(true)
              .condiciones(DummyDataGenerator.generarCondicionesRandom(RANDOM.nextInt(5) + 1))
              .build();
            esquemas.add(esquemaPago);
        }

        for (int i = habilitadosMin; i < cantidad; i++) {

            // Generar datos básicos
            String baseName = obtenerNombreBaseAleatorio();
            int numeroPagos = RANDOM.nextInt(24) + 1; // Pagos entre 1 y 24
            String frecuenciaCobro = obtenerFrecuenciaAleatoria();
            String nombre = generarNombreEsquema(baseName, numeroPagos, frecuenciaCobro);
            EsquemaPago esquemaPago = EsquemaPago.builder()
              .id((i + 1))
              .nombre(nombre)
              .numeroPagos(numeroPagos)
              .frecuenciaCobro(frecuenciaCobro)
              .tasa(RANDOM.nextDouble() * 5) // tasa netre 0.0 y 5.0
              .habilitado(RANDOM.nextBoolean())
              .condiciones(DummyDataGenerator.generarCondicionesRandom(RANDOM.nextInt(5) + 1))
              .build();
            esquemas.add(esquemaPago);
        }

        return esquemas;
    }

    private static String obtenerNombreBaseAleatorio() {
        return BASE_NAMES[RANDOM.nextInt(BASE_NAMES.length)];
    }

    private static String obtenerFrecuenciaAleatoria() {
        return FRECUENCIAS[RANDOM.nextInt(FRECUENCIAS.length)];
    }

    /**
     * Genera una lista de nombres de esquemas aleatorios.
     *
     * @param cantidad Número de nombres a generar.
     * @return Lista de nombres de esquemas.
     */
    public static List<String> generarNombresEsquemaRandom(int cantidad) {
        List<String> nombres = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            String baseName = BASE_NAMES[RANDOM.nextInt(BASE_NAMES.length)];
            int numeroPagos = RANDOM.nextInt(24) + 1; // Pagos entre 1 y 24
            String frecuencia = FRECUENCIAS[RANDOM.nextInt(FRECUENCIAS.length)];
            nombres.add(generarNombreEsquema(baseName, numeroPagos, frecuencia));
        }
        return nombres;
    }

    /**
     * Genera una lista de CondicionRegla con datos aleatorios para pruebas.
     *
     * @param cantidad Número de objetos CondicionRegla a generar.
     * @return Lista de CondicionRegla con datos aleatorios.
     */
    public static List<CondicionRegla> generarCondicionesRandom(int cantidad) {
        List<CondicionRegla> condiciones = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            CondicionRegla condicionRegla = CondicionRegla.builder()
              .id((i + 1))
              .clave(CLAVES[RANDOM.nextInt(CLAVES.length)])
              .valor(String.valueOf(RANDOM.nextInt(100) + 1))
              .habilitado(RANDOM.nextBoolean())
              .operador(OPERADORES[RANDOM.nextInt(OPERADORES.length)])
              .build();
            condiciones.add(condicionRegla);
        }

        return condiciones;
    }


    /**
     * Genera un nombre de esquema basado en los parámetros.
     *
     * @param baseName        Nombre base.
     * @param numeroPagos     Número de pagos.
     * @param frecuenciaCobro Frecuencia de cobro.
     * @return Nombre del esquema.
     */
    public static String generarNombreEsquema(String baseName, int numeroPagos, String frecuenciaCobro) {
        return String.format("%s_%d_%s", baseName.toUpperCase(), numeroPagos, frecuenciaCobro.toUpperCase());
    }

    public static ClienteRequest getClienteRequest() {
        return ClienteRequest.builder()
          .idCliente(1L)
          .nombre("Test")
          .apMaterno("apmaterno")
          .apPaterno("appaterno")
          .fechaNacimiento("2000/03/12")
          .build();
    }

    public static Cliente getCliente() {
        LocalDateTime now = LocalDateTime.now();
        Cliente cliente = Cliente.builder()
          .id(1L)
          .nombre("Test")
          .apMaterno("apmaterno")
          .apPaterno("appaterno")
          .fechaNacimiento(LocalDate.of(2000, 3, 12))
          .build();

        LineaCredito lineaCredito = LineaCredito.builder()
          .montoAsignado(5000.0)
          .cliente(cliente)
          .fechaActualizacion(now)
          .fechaRegistro(now)
          .id(1L)
          .build();

        cliente.setLineasCredito(List.of(lineaCredito));

        return cliente;


    }
}
