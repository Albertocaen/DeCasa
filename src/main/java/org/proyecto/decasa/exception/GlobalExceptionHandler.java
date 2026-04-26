package org.proyecto.decasa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Centraliza el manejo de errores de TODA la aplicación.
 *
 * @RestControllerAdvice intercepta excepciones lanzadas en cualquier
 * @RestController y las convierte en respuestas HTTP estructuradas,
 * evitando que cada controlador tenga su propio try-catch.
 *
 * Usamos ProblemDetail (RFC 9457) — el estándar moderno de Spring para
 * respuestas de error en APIs REST. Tiene campos: type, title, status, detail.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 404 — Recurso no encontrado.
     * Ej: GET /api/products/99 cuando el producto no existe.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * 400 — Regla de negocio violada.
     * Ej: fecha de entrega en menos de 48h, cantidad mínima no alcanzada.
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRule(BusinessRuleException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * 400 — Validación de campos del request fallida.
     *
     * Cuando un DTO tiene anotaciones @NotBlank, @NotNull, @Min, etc.
     * y el cliente manda un valor inválido, Spring lanza esta excepción.
     *
     * Devolvemos un mapa campo → mensaje de error para que el frontend
     * pueda mostrar los errores en el formulario correcto.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Valor inválido"
                ));

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Hay errores de validación en la solicitud"
        );
        // Añadimos los errores por campo como propiedad extra del ProblemDetail
        problem.setProperty("errors", fieldErrors);
        return problem;
    }
}
