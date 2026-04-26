package org.proyecto.decasa.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.proyecto.decasa.api.dto.request.LoginRequest;
import org.proyecto.decasa.api.dto.response.AuthResponse;
import org.proyecto.decasa.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * POST /api/auth/login → devuelve un JWT si las credenciales son correctas.
 *
 * Flujo de autenticación:
 *   1. Recibimos username + password en el body
 *   2. authenticationManager.authenticate() verifica las credenciales
 *      usando nuestro UserDetailsService y BCryptPasswordEncoder
 *   3. Si son correctas, generamos un JWT y lo devolvemos
 *   4. El cliente guarda el token y lo manda en cada petición admin
 *
 * Si las credenciales son incorrectas, Spring lanza BadCredentialsException
 * que se traduce a 401 Unauthorized automáticamente.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        // Intentamos autenticar — si falla lanza excepción (401)
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        // Generamos el token con el username del principal autenticado
        String token = jwtTokenProvider.generateToken(auth.getName());

        return ResponseEntity.ok(new AuthResponse(token, auth.getName()));
    }
}
