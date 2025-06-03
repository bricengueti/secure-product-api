package com.TNBtech.secure_product_api.controller;
import com.TNBtech.secure_product_api.DTO.RegisterRequestDto;
import com.TNBtech.secure_product_api.DTO.RegisterResponseDto;
import com.TNBtech.secure_product_api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Enregistrement", description = "Endpoints pour enregistrer un nouvel utilisateur.")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Créer un nouvel utilisateur",
            description = "Enregistre un nouvel utilisateur avec les informations fournies (nom complet, email, mot de passe, etc.)."
    )
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
        RegisterResponseDto response = userService.register(request);
        return ResponseEntity.ok(response);
    }
}
