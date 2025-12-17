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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    SocioRepository socioRepository;

    @Mock
    EntrenadorRepository entrenadorRepository;

    @InjectMocks
    UserService userService;
    Role roleAdmin;
    Role roleSocio;
    Role roleEntrenador;
    User user1;
    User user2;
    UserPublicRequestDTO userPublicRequest;
    UserStaffRequestDTO userStaffRequest;

    @BeforeEach
    void setUp() {
        this.roleAdmin = new Role();
        roleAdmin.setId(1);
        roleAdmin.setName("ROLE_ADMIN");

        this.roleSocio = new Role();
        roleSocio.setId(2);
        roleSocio.setName("ROLE_SOCIO");

        this.roleEntrenador = new Role();
        roleEntrenador.setId(3);
        roleEntrenador.setName("ROLE_ENTRENADOR");

        Set<Role> roles1 = new HashSet<>();
        roles1.add(roleAdmin);

        Set<Role> roles2 = new HashSet<>();
        roles2.add(roleSocio);

        this.user1 = new User();
        this.user1.setId(1);
        this.user1.setEmail("test@gym.com");
        this.user1.setRoles(roles1);
        this.user1.setPassword("password123");

        this.user2 = new User();
        this.user2.setId(2);
        this.user2.setEmail("test@gmail.com");
        this.user2.setRoles(roles2);
        this.user2.setPassword("password123");

        this.userStaffRequest = new UserStaffRequestDTO("entrenador@gym.com", user1.getPassword(), Set.of("ROLE_ADMIN","ROLE_ENTRENADOR"), "Julia", "1198336655");
        this.userPublicRequest = new UserPublicRequestDTO("test@gmail.com", user2.getPassword(), "Flor", "1122336655");

    }

    @Test
    @DisplayName("Obtener todos los Usuarios (OK)")
    void obtenerTodosLosUsuariosExitoso() {
        List<User> users = Arrays.asList(this.user1,  this.user2);

        when(userRepository.findAll()).thenReturn(users);

        List<UserResponseDTO> resultado = userService.findAll();

        assertNotNull(resultado);
        assertEquals(users.size(), resultado.size());

        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Obtener todos los Usuarios (Vacío)")
    void obtenerTodosLosUsuariosVacio(){
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserResponseDTO> resultado = userService.findAll();
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Registro de Usuario Público (OK)")
    void registrarUsuarioPublicoExitoso() {

        when(userRepository.existsByEmail(userPublicRequest.getEmail())).thenReturn(false);
        when(roleRepository.findByName("ROLE_SOCIO")).thenReturn(Optional.of(roleSocio));
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(1);
            return u;
        });

        UserResponseDTO resultado = userService.registerUserPublic(userPublicRequest);

        assertNotNull(resultado);

        verify(userRepository).existsByEmail(userPublicRequest.getEmail());
        verify(userRepository).save(any(User.class));
        verify(socioRepository).save(any(Socio.class));

    }

    @Test
    @DisplayName("Registro de Usuario Público (FALLA) - Email duplicado")
    void registrarUsuarioPublicoFalla() {
        when(userRepository.existsByEmail(userPublicRequest.getEmail())).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> userService.registerUserPublic(userPublicRequest));

        verify(userRepository).existsByEmail(any());
        verify(userRepository,never()).save(any(User.class));
    }

    @Test
    @DisplayName("Registro de Usuario Staff (OK) - Perfil Entrenador")
    void  registrarUsuarioStaffEntrenadorExitoso() {
        userStaffRequest.setRoles(Set.of("ROLE_ENTRENADOR"));

        when(userRepository.existsByEmail(userStaffRequest.getEmail())).thenReturn(false);
        when(roleRepository.findByName("ROLE_ENTRENADOR")).thenReturn(Optional.of(roleEntrenador));
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(1);
            return u;
        });

        when(entrenadorRepository.existsByContacto(userStaffRequest.getTelefono())).thenReturn(false);

        UserResponseDTO resultado = userService.registerUserStaff(userStaffRequest);

        assertNotNull(resultado);
        assertTrue(resultado.getRoles().contains(roleEntrenador.getName()));
        verify(userRepository, times(2)).save(any(User.class));
        verify(entrenadorRepository).save(any(Entrenador.class));
    }

    @Test
    @DisplayName("Registro de Usuario Staff (OK) - Perfil Admin")
    void  registrarUsuarioStaffAdminExitoso() {
        userStaffRequest.setRoles(Set.of("ROLE_ADMIN"));

        when(userRepository.existsByEmail(userStaffRequest.getEmail())).thenReturn(false);
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(roleAdmin));
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(1);
            return u;
        });

        UserResponseDTO resultado = userService.registerUserStaff(userStaffRequest);

        assertNotNull(resultado);
        assertTrue(resultado.getRoles().contains(roleAdmin.getName()));

        verify(userRepository, times(2)).save(any(User.class));
        verify(entrenadorRepository, never()).save(any(Entrenador.class));
    }

    @Test
    @DisplayName("Registro de Usuario Staff (FALLA) - Rol no existe")
    void registrarUsuarioStaffFallo() {
        userStaffRequest.setRoles(Set.of("ROLE_USER"));

        when(userRepository.existsByEmail(userStaffRequest.getEmail())).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.registerUserStaff(userStaffRequest));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Registro de Usuario Staff (FALLA) - Email duplicado ")
    void registrarUsuarioStaffEntrenadorFallo() {
        when(userRepository.existsByEmail(userStaffRequest.getEmail())).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> userService.registerUserStaff(userStaffRequest));

        verify(userRepository).existsByEmail(any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Registro de Usuario Staff (FALLA) - Contacto duplicado")
    void registrarUsuarioStaffContactoFallo() {
        userStaffRequest.setRoles(Set.of("ROLE_ENTRENADOR"));

        when(userRepository.existsByEmail(userStaffRequest.getEmail())).thenReturn(false);
        when(roleRepository.findByName("ROLE_ENTRENADOR")).thenReturn(Optional.of(roleEntrenador));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        when(entrenadorRepository.existsByContacto(userStaffRequest.getTelefono())).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> userService.registerUserStaff(userStaffRequest));

        verify(entrenadorRepository, never()).save(any(Entrenador.class));
    }

    @Test
    @DisplayName("LoadUserByUsername (OK) - Retorna UserDetails")
    void loadUserByUsernameExitoso() {
        String email = "test@gym.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user1));

        UserDetails userDetails = userService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(user1.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("LoadUserByUsername (FALLA) - Usuario no encontrado")
    void loadUserByUsernameNoEncontrado() {
        String email = "notfound@gym.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));
    }
}