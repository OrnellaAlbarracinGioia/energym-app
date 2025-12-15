package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.request.UserPublicRequestDTO;
import com.energym.energym_reservas.dto.request.UserStaffRequestDTO;
import com.energym.energym_reservas.dto.response.UserResponseDTO;
import com.energym.energym_reservas.entity.Entrenador;
import com.energym.energym_reservas.entity.Role;
import com.energym.energym_reservas.entity.Socio;
import com.energym.energym_reservas.entity.User;
import com.energym.energym_reservas.exception.BusinessRuleException;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.repository.EntrenadorRepository;
import com.energym.energym_reservas.repository.RoleRepository;
import com.energym.energym_reservas.repository.SocioRepository;
import com.energym.energym_reservas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SocioRepository socioRepository;
    private final EntrenadorRepository entrenadorRepository;

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();

        List<UserResponseDTO> usersDTO = users.stream().map(user -> UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .enabled(user.isEnabled())
                .build())
                .toList();

        return usersDTO;
    }

    public UserResponseDTO registerUserPublic(UserPublicRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException("El email ingresado ya existe");
        }

        Set<Role> roles = new HashSet<>();
        Optional<Role> rolSocio = roleRepository.findByName("ROLE_SOCIO");

        rolSocio.ifPresent(roles::add);

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .roles(roles)
                .build();

        User savedUser = userRepository.save(user);

        createProfileSocio(savedUser, request);

        return UserResponseDTO.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .roles(savedUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .enabled(savedUser.isEnabled())
                .build();
    }

    public UserResponseDTO registerUserStaff(UserStaffRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException("El email ingresado ya existe");
        }

        //Se obtienen los roles asignados que llegan por el request
        Set<Role> roles = request.getRoles().stream()
                .map(nombreRol -> roleRepository.findByName(nombreRol)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "name", nombreRol)))
                .collect(Collectors.toSet());

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .roles(roles)
                .build();

        User savedUser = userRepository.save(user);

        if (roles.stream().anyMatch(role -> role.getName().equals("ROLE_ENTRENADOR"))) {
            createProfileEntrenador(savedUser, request);
        }

        userRepository.save(savedUser);

        return UserResponseDTO.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .roles(savedUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .enabled(savedUser.isEnabled())
                .build();
    }

    private void createProfileSocio(User savedUser, UserPublicRequestDTO request) {
        Socio nuevo = Socio.builder()
                .nombre(request.getNombre())
                .telefono(request.getTelefono())
                .user(savedUser)
                .build();
        socioRepository.save(nuevo);
    }

    private void createProfileEntrenador(User savedUser, UserStaffRequestDTO request) {

        if(entrenadorRepository.existsByContacto(request.getTelefono())){
            throw new BusinessRuleException("Ya existe un entrenador con ese contacto");
        }

        Entrenador nuevo = Entrenador.builder()
                .nombre(request.getName())
                .contacto(request.getTelefono())
                .user(savedUser)
                .build();
        entrenadorRepository.save(nuevo);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username){
        Optional<User> userOpt = userRepository.findByEmail(username);

        if(userOpt.isEmpty()) {
            throw new UsernameNotFoundException("El Email ingresado no existe");
        }

        User user =  userOpt.orElseThrow();

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                authorities);
    }
}
