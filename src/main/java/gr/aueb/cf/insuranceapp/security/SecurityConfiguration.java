package gr.aueb.cf.insuranceapp.security;

import gr.aueb.cf.insuranceapp.authentication.JwtAuthenticationFilter;
import gr.aueb.cf.insuranceapp.core.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(myCustomAuthenticationEntryPoint()))
                .exceptionHandling(exceptions -> exceptions.accessDeniedHandler(myCustomAccessDeniedHandler()))
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/api/customers/save").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/customers/paginated").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/customers/filtered").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/customers/filtered/paginated").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/customers/afm/{afm}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/customers/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/auth/authenticate").permitAll()
                        .requestMatchers("/api/policies/create").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/policies/{uuid}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/policies/{uuid}/update-start-date").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/policies/paginated").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/policies/filtered").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/policies/filtered/paginated").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/policies/customer/self").hasAnyAuthority(Role.ADMIN.name(), Role.CUSTOMER.name())
                        .requestMatchers("/api/vehicles/cars").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/vehicles/motorcycles").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/**").permitAll()
                )
                .sessionManagement((session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("https://coding-factory.apps.gov.gr",
                "https://test-coding-factory.apps.gov.gr", "http://localhost:4200", "http://localhost:5173"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }


    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint myCustomAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler myCustomAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}