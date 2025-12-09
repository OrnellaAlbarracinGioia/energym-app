package com.energym.energym_reservas.security.filter;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.Jwts;

import static com.energym.energym_reservas.security.JwtTokenConfig.*;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    //DTO mini interno para no exponer la entidad directamente
    @Getter
    @Setter
    private static class AuthRequest {
        private String email;
        private String password;
    }

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/api/auth/login");
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AuthRequest authRequest  = null;
        String username = null;
        String password = null;

        try {
            authRequest = new ObjectMapper().readValue(request.getInputStream(), AuthRequest.class);
            username = authRequest.getEmail();
            password = authRequest.getPassword();
        } catch (IOException e) {
            throw new RuntimeException("Error al leer las credenciales",e);
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();

        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

        Claims claims = Jwts.claims()
                .add("authorities", new ObjectMapper().writeValueAsString(roles))
                .build();

        String token = Jwts.builder()
                .subject(userDetails.getUsername())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) //Es válido por 1 hs
                .issuedAt(new Date())
                .claims(claims)
                .signWith(SECRET_KEY)
                .compact();

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

        Map<String, String> body = new HashMap<>();
        body.put("token", token);
        body.put("username", userDetails.getUsername());
        body.put("message", String.format("Hola %s iniciaste sesión exitosamente", userDetails.getUsername()));

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException{

        Map<String, String> body = new HashMap<>();
        body.put("message", "Error de Autenticación: Username o Password incorrectos");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(401);
    }
}
