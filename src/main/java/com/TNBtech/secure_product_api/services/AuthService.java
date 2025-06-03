package com.TNBtech.secure_product_api.services;
import com.TNBtech.secure_product_api.DTO.LoginRequestDto;
import com.TNBtech.secure_product_api.DTO.RefreshTokenRequestDto;
import com.TNBtech.secure_product_api.DTO.TokenResponseDto;
import com.TNBtech.secure_product_api.entities.RefreshToken;
import com.TNBtech.secure_product_api.entities.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthService(JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       UserService userService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    // Authentifie l'utilisateur et génère les tokens
    public TokenResponseDto login(LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        User user = (User) authentication.getPrincipal();
        Map<String, String> tokenMap = jwtService.generate(user.getUsername());

        return new TokenResponseDto(
                tokenMap.get("Bearer"),
                tokenMap.get("refresh_token")
        );
    }

    // Rafraîchit le JWT à partir du refresh token
    public TokenResponseDto refreshToken(RefreshTokenRequestDto request) {
        RefreshToken refreshToken = jwtService.validateRefreshToken(request.refreshToken());
        Map<String, String> tokenMap = jwtService.generate(refreshToken.getUser().getUsername());

        return new TokenResponseDto(
                tokenMap.get("Bearer"),
                tokenMap.get("refresh_token")
        );
    }

    // Déconnecte l'utilisateur
    public void logout() {
        jwtService.logout();
    }
}
