package tech.bnpl.apionline.controller.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String YYYY_MM_DD = "El formato de fecha no es valido. Use yyyy-MM-dd";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(final MethodArgumentNotValidException exception) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Error de Validacion");
        apiError.addValidationErrors(exception.getBindingResult().getFieldErrors());
        apiError.addValidationError(exception.getBindingResult().getGlobalErrors());
        return new ResponseEntity<>(apiError, apiError.getStatus());

    }

    @ExceptionHandler(LineaCreditoException.class)
    public ResponseEntity<Object> handleLineaCreditoException(final LineaCreditoException exception) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception);
        apiError.setMessage("Se ha producido un error, al momento de asignar la linea de credito para el usuario");
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(final EntityNotFoundException exception) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception);
        apiError.setMessage("Se ha producido un error, el registro no existe");
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleException(final Exception exception) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);

        Throwable rootCause = exception.getCause();
        if (rootCause instanceof InvalidFormatException invalidFormatException && invalidFormatException.getTargetType().equals(java.time.LocalDate.class)) {
            apiError.setMessage(YYYY_MM_DD);
            FieldError fieldError = new FieldError(
              "clienteRequest",
              invalidFormatException.getPath().get(0).getFieldName(),
              YYYY_MM_DD
            );
            apiError.addValidationErrors(List.of(fieldError));
            return new ResponseEntity<>(apiError, apiError.getStatus());
        }

        apiError.setMessage("Error en la solicitud");
        FieldError fieldError = new FieldError(
          "clienteRequest",
          "",
          YYYY_MM_DD
        );
        apiError.addValidationErrors(List.of(fieldError));
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
