package com.digitalMarket.iamService.presentation.controllers;

import com.digitalMarket.iamService.application.dto.request.LoginRequest;
import com.digitalMarket.iamService.application.dto.response.LoginResponse;
import com.digitalMarket.iamService.application.service.AuthService;
import com.digitalMarket.iamService.infraestructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/usuario/{email}/rol/{rolId}")
    public String asignarRolAUsuario(@PathVariable String email, @PathVariable Integer rolId) {
        authService.asignarRolAUsuario(email, rolId);
        return "Rol asignado correctamente";
    }

    @PostMapping("/rol/{rolId}/permiso/{permisoId}")
    public String asignarPermisoARol(@PathVariable Integer rolId, @PathVariable Integer permisoId) {
        authService.asignarPermisoARol(rolId, permisoId);
        return "Permiso asignado correctamente";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        Authentication user = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = jwtService.generateToken((UserDetails) user.getPrincipal());

        return ResponseEntity.ok(
                new LoginResponse(token)
        );
    }
}
