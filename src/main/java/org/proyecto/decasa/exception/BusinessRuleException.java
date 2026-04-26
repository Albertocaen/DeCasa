package org.proyecto.decasa.exception;

/**
 * Se lanza cuando se viola una regla de negocio.
 * Ejemplos: pedido con menos de 48h de antelación,
 *           cantidad por debajo del mínimo de unidades,
 *           importe mínimo no alcanzado.
 *
 * El GlobalExceptionHandler la mapea a HTTP 400 Bad Request.
 */
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
