package tech.bnpl.apionline.core.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tech.bnpl.apionline.core.BpnlStringUtils;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class ValidBirthDateValidator implements ConstraintValidator<ValidBirthDate, String> {

    @Override
    public boolean isValid(String fechaNacimiento, ConstraintValidatorContext context) {
        if (Objects.isNull(fechaNacimiento)) {
            return false;
        }
        LocalDate localDate = BpnlStringUtils.toLocalDate(fechaNacimiento);
        LocalDate hoy = LocalDate.now();
        Period edad = Period.between(localDate, hoy);
        return edad.getYears() >= 18 && edad.getYears() <= 90;
    }
}
