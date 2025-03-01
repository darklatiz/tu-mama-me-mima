package tech.bnpl.apionline.core;

import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class BpnlStringUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private BpnlStringUtils() {}

    /**
     * Converts a java variable name (camel case convention) to a snake case representation
     *
     * @param camelCaseString Java variable name
     * @return snake case string
     */
    public static Optional<String> fromCamelCaseToSnakeCase(String camelCaseString) {
        if (StringUtils.isEmpty(camelCaseString) || StringUtils.isBlank(camelCaseString)) return Optional.empty();
        var stringBuilder = new StringBuilder();
        for (int i = 0; i < camelCaseString.length(); i++) {
            char charAt = camelCaseString.charAt(i);

            if (Character.isUpperCase(charAt)) {
                stringBuilder.append("_");
                charAt = Character.toLowerCase(charAt);
            }
            stringBuilder.append(charAt);
        }
        return Optional.of(stringBuilder.toString());

    }

    public static LocalDate toLocalDate(@NotNull(message = "La fecha de nacimiento es obligatorio (YYYY/MM/DD)") String fechaNacimiento) {
        return LocalDate.parse(fechaNacimiento, formatter);
    }

    public static LocalDate toLocalDateFromYear(String year) {
        return LocalDate.of(Integer.parseInt(year), 1, 1);
    }

    public static @NotNull(message = "La fecha de nacimiento es obligatorio (YYYY/MM/DD)") String fromDateToString(LocalDate localDate) {
        return localDate.format(formatter);
    }
}
