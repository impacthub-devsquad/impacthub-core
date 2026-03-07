package br.social.impacthub.config.security;

import br.social.impacthub.service.TokenService;
import br.social.impacthub.service.UserCredentialsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private TokenService tokenService;

    private UserCredentialsService userCredentialsService;

    public AuthenticationFilter(TokenService tokenService, UserCredentialsService userCredentialsService) {
        this.tokenService = tokenService;
        this.userCredentialsService = userCredentialsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = retrieveToken(request);

        if (token != null && !token.isBlank())
            authenticateUser(token);

        filterChain.doFilter(request, response);
    }

    private String retrieveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    private void authenticateUser(String token) {
        String login = tokenService.validateTokenAndRetrieveUsername(token);
        UserDetails userDetails = userCredentialsService.loadUserByUsername(login);
        var userPassToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(userPassToken);
    }
}
