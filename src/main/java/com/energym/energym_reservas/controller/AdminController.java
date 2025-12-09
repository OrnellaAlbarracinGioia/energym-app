package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.request.UserStaffRequestDTO;
import com.energym.energym_reservas.dto.response.UserResponseDTO;
import com.energym.energym_reservas.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "API exclusiva para Administración")
public class AdminController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserStaffRequestDTO request) {
        return ResponseEntity.ok().body(userService.registerUserStaff(request));
    }

}
