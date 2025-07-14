package gr.aueb.cf.insuranceapp.authentication;

import gr.aueb.cf.insuranceapp.security.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization"); //gets the header from the HTTP Request
        String jwt;
        String username;
        String userRole;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // if the header does not exist or it does not start with "Bearer"
            filterChain.doFilter(request, response); // do not authenticate
            return;
        }
        jwt = authHeader.substring(7); // getting the token

        try {
            username = jwtService.extractSubject(jwt); //username
            userRole = jwtService.getStringClaim(jwt, "role");

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { // if username exists but is not authenticated
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) { // if token is valid (for example it has not expired)
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken( // then authenticate it
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    LOGGER.warn("Token is not valid" + request.getRequestURI());
                }
            }
        } catch (ExpiredJwtException e) {
            LOGGER.warn("WARN: Expired token ", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            String jsonBody = "{\"code\": \"expired token\", \"message\"" + e.getMessage() + "\"}";
            response.getWriter().write(jsonBody);
            return;
        } catch (Exception e) {
            LOGGER.warn("WARN: Something went wrong while parsing JWT ", e);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            String jsonBody = "{\"code\": \"invalidToken\", \"description\"" + e.getMessage() + "\"}";
            response.getWriter().write(jsonBody);
            return;
        }
        filterChain.doFilter(request, response); // if we have not other exceptions then pass the request to the controller
    }
}