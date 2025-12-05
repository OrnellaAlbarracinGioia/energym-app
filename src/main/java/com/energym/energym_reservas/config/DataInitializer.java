package com.energym.energym_reservas.config;

import com.energym.energym_reservas.entity.Role;
import com.energym.energym_reservas.entity.User;
import com.energym.energym_reservas.repository.RoleRepository;
import com.energym.energym_reservas.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Puedes configurar esto en application.properties o usar estos defaults
    @Value("${app.admin.email:admin@gym.com}")
    private String adminEmail;

    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    @Override
    @Transactional
    public void run(String... args) {

        Role roleAdmin = crearRolSiNoExiste("ROLE_ADMIN");
        Role roleEntrenador = crearRolSiNoExiste("ROLE_ENTRENADOR");
        Role roleSocio = crearRolSiNoExiste("ROLE_SOCIO");

        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .enabled(true)
                    .roles(Set.of(roleAdmin))
                    .build();

            userRepository.save(admin);
        }
    }

    private Role crearRolSiNoExiste(String nombre) {
        return roleRepository.findByName(nombre).orElseGet(() -> {
            Role rol = new Role();
            rol.setName(nombre);
            return roleRepository.save(rol);
        });
    }

}
