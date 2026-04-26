package org.proyecto.decasa.exception;

/**
 * Se lanza cuando no se encuentra un recurso en la base de datos.
 * El GlobalExceptionHandler la captura y devuelve HTTP 404.
 *
 * Extiende RuntimeException para no obligar al caller a capturarla
 * (unchecked exception) — en Spring es la convención estándar.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
