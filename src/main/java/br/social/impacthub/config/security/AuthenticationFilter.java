package br.social.impacthub.config.security;

import br.social.impacthub.exception.InvalidAccessTokenException;
import br.social.impacthub.exception.UserNotExistsException;
import br.social.impacthub.exception.WrongCredentialsException;
import br.social.impacthub.model.entity.UserCredentials;
import br.social.impacthub.service.security.AuthService;
import br.social.impacthub.service.security.TokenService;
import br.social.impacthub.service.security.UserCredentialsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private final UserCredentialsService userCredentialsService;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @Autowired @Lazy
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    public AuthenticationFilter(UserCredentialsService userCredentialsService, AuthService authService, @Lazy AuthenticationManager authenticationManager) {
        this.userCredentialsService = userCredentialsService;
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = retrieveToken(request);

        if (token != null && !token.isBlank()) {
            try {
                authenticateUser(token);
            } catch (InvalidAccessTokenException exception) {
                exceptionResolver.resolveException(request, response, null, exception);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String retrieveToken(HttpServletRequest request) {
        try {
            return request.getHeader("Authorization").replace("Bearer ", "").trim();
        }
        catch (Exception e){
            return null;
        }
    }

    private void authenticateUser(String token) {
        authService.validateAccessToken(token);


        UserCredentials user = userCredentialsService.getById(
                authService.getUserId(token)
        );
        var userPassToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(userPassToken);
    }
}
