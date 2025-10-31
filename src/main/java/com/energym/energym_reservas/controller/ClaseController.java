package com.energym.energym_reservas.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clases")
@RequiredArgsConstructor
@Tag(name = "clases", description = "API para gestión de clases del gimnasio")
public class ClaseController {
}
