package org.proyecto.decasa.api.dto.response;

/**
 * Respuesta del endpoint POST /api/auth/login.
 * Devuelve el JWT que el cliente debe incluir en
 * el header "Authorization: Bearer <token>" en todas las
 * llamadas a rutas admin.
 */
public record AuthResponse(String token, String username) {}
