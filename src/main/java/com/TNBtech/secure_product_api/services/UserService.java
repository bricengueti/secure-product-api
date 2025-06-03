package com.TNBtech.secure_product_api.services;

import com.TNBtech.secure_product_api.DTO.RegisterRequestDto;
import com.TNBtech.secure_product_api.DTO.RegisterResponseDto;
import com.TNBtech.secure_product_api.entities.User;
import com.TNBtech.secure_product_api.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));
    }

    @Transactional
    public RegisterResponseDto register(RegisterRequestDto request) {
        // Vérifier si l’utilisateur existe déjà
        if (userRepository.existsByUsername(request.username()) || userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Nom d'utilisateur ou email déjà utilisé");
        }

      User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setFullName(request.fullName());
        userRepository.save(user);

        return new RegisterResponseDto(
                user.getFullName(),
                user.getUsername(),
                user.getEmail()
        );
    }
}

