package org.proyecto.decasa.api.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Cuerpo de la petición POST /api/auth/login.
 *
 * Usamos record (Java 16+) para DTOs de entrada/salida inmutables:
 * es más conciso que una clase con @Getter/@Setter/@AllArgsConstructor.
 *
 * @NotBlank valida que el campo no sea null ni esté vacío.
 * Si falla, GlobalExceptionHandler devuelve 400 con el detalle del campo.
 */
public record LoginRequest(
        @NotBlank(message = "El email es obligatorio") String username,
        @NotBlank(message = "La contraseña es obligatoria") String password
) {}
