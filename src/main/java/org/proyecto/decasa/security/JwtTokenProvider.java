package org.proyecto.decasa.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Responsable de crear y validar tokens JWT.
 *
 * ¿Qué es un JWT?
 * Es un string con tres partes separadas por puntos: header.payload.signature
 *   - Header:    algoritmo de firma (HS256)
 *   - Payload:   "claims" — datos que metemos en el token (username, expiración)
 *   - Signature: el header y payload firmados con nuestra clave secreta
 *
 * El servidor nunca guarda el token — solo verifica que la firma sea válida.
 * Si alguien modifica el payload, la firma no coincide y el token se rechaza.
 */
@Component
public class JwtTokenProvider {

    /**
     * @Value inyecta el valor desde application.properties.
     * La clave debe tener al menos 256 bits (32 caracteres) para HS256.
     */
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    /**
     * Genera un token JWT para el usuario autenticado.
     * El token contiene:
     *   - subject: el username
     *   - issuedAt: cuándo se creó
     *   - expiration: cuándo caduca
     */
    public String generateToken(String username) {
        Date now    = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrae el username del token.
     * Si el token es inválido o está expirado, Jwts lanza JwtException.
     */
    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Valida el token: firma correcta + no expirado.
     * Devuelve false en lugar de lanzar excepción para usarlo en el filtro.
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Convierte el secret string a una SecretKey criptográfica.
     * Keys.hmacShaKeyFor garantiza que la clave sea válida para HS256.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
