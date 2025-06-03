package com.TNBtech.secure_product_api.controller;

import com.TNBtech.secure_product_api.DTO.LoginRequestDto;
import com.TNBtech.secure_product_api.DTO.RefreshTokenRequestDto;
import com.TNBtech.secure_product_api.DTO.TokenResponseDto;
import com.TNBtech.secure_product_api.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentification", description = "Endpoints liés à l'authentification, à la génération des tokens JWT et à la déconnexion.")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Authentifier un utilisateur",
            description = "Vérifie les identifiants de l'utilisateur et retourne un JWT valide ainsi qu'un refresh token."
    )
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        TokenResponseDto token = authService.login(loginRequestDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Rafraîchir le token JWT",
            description = "Génère un nouveau JWT à partir d’un refresh token valide et non expiré."
    )
    public ResponseEntity<TokenResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto request) {
        TokenResponseDto token = authService.refreshToken(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Déconnecter l'utilisateur",
            description = "Désactive le JWT actif de l'utilisateur connecté. Ce token ne pourra plus être utilisé."
    )
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.noContent().build();
    }
}
