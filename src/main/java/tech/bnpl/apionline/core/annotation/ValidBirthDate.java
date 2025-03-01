package tech.bnpl.apionline.core.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ValidBirthDateValidator.class)
public @interface ValidBirthDate {
    String message() default "La fecha de nacimiento no cumple con el formato (YYYY/MM/DD) o La edad es menor a 18 o Mayor a 90 Anios";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
