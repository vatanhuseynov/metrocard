package com.ibar.metrocard.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibar.metrocard.utility.JwtGenerateUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
    private final JwtGenerateUtil jwtGenerateUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!request.getServletPath().contains("public")) {
            var token = getToken(request);
            if (token != null) {
                try {
                    var decodedAccessToken = jwtGenerateUtil.decodeToken(token);
                    var authorities = decodedAccessToken.getRoles().stream().map(SimpleGrantedAuthority::new).toList();
                    var authenticationToken = new UsernamePasswordAuthenticationToken(decodedAccessToken.getUsername(), null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } catch (Exception e) {
                    handleException(response, e);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);

    }

    private String getToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring("Bearer ".length());
        }
        return request.getParameter("token");
    }

    private void handleException(HttpServletResponse response,Exception exception) throws IOException {
        log.error("Error logging in {}", exception.getMessage());
        response.setHeader("error", exception.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, String> error = new HashMap<>();
        error.put("error_message", exception.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}
