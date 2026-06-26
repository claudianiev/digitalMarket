package com.digitalMarket.iamService.infraestructure.security.config;


import com.digitalMarket.iamService.infraestructure.security.JwtAuthenticationFilter;
import com.digitalMarket.iamService.infraestructure.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtService, userDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtAuthenticationFilter jwtFilter,
                                           AuthenticationProvider authProvider) throws Exception {
        System.out.println("JWT FILTER EJECUTADO");
        System.out.println(">>> SECURITY CONFIG LOADED");
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .authorizeHttpRequests(auth -> {
                            auth
                                    .requestMatchers("/auth/login", "/error")
                                    .permitAll()

                                    // USERS
                                    .requestMatchers(
                                            HttpMethod.POST, "/users"
                                    ).hasAuthority("ADMIN")

                                    .requestMatchers(
                                            HttpMethod.GET, "/users/**"
                                    ).hasAuthority("ADMIN")

                                    .requestMatchers(
                                            HttpMethod.PUT, "/users/**"
                                    ).hasAuthority("ADMIN")

                                    .requestMatchers(
                                            HttpMethod.DELETE, "/users/**"
                                    ).hasAuthority("ADMIN")

                                    // ROLE
                                    .requestMatchers("/role/**")
                                    .hasAuthority("ADMIN")

                                    // PERMITS
                                    .requestMatchers("/permits/**")
                                    .hasAuthority("ADMIN")

                                    // AUTH
                                    .requestMatchers(
                                            "/auth/usuario/**",
                                            "/auth/rol/**"
                                    ).hasAuthority("ADMIN")

                                    // PRODUCTS
                                    .requestMatchers("/products/**")
                                    .hasAnyAuthority("USER","ADMIN")

                                    .anyRequest().authenticated();
                        }
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                (req, res, ex) ->
                                        res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                        .accessDeniedHandler(
                                (req, res, ex) ->
                                        res.sendError(HttpServletResponse.SC_FORBIDDEN)))
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}