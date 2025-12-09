package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.request.UserPublicRequestDTO;
import com.energym.energym_reservas.dto.response.UserResponseDTO;
import com.energym.energym_reservas.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "API destinada a la autenticación pública")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserPublicRequestDTO request){
        return ResponseEntity.ok().body(userService.registerUserPublic(request));
    }

    /*@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO dto) {
        //TODO
        return ResponseEntity.ok("Login pendiente de implementación");
    }*/
}
