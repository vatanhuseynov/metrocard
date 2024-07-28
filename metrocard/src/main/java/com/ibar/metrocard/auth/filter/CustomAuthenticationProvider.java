package com.ibar.metrocard.auth.filter;

import com.ibar.metrocard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static com.ibar.metrocard.exception.StatusMessage.USERNAME_OR_PASSWORD_WRONG;


@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = (String) authentication.getPrincipal();
        var user = userService.loadByUsername(username);
        var password = String.valueOf(authentication.getCredentials());
        if (user == null || !passwordEncoder.matches(password, user.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USERNAME_OR_PASSWORD_WRONG.getMessage());
        }
        return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
