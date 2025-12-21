package com.gestionseguridad.venta_maquinaria.securities;

import com.gestionseguridad.venta_maquinaria.models.User;
import com.gestionseguridad.venta_maquinaria.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserService userService,
                          @Lazy JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userService = userService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userService.getUserByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRol().name())
                    .build();
        };
    }

    @java.lang.SuppressWarnings("java:S4502")
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**") 
            )
            .authorizeHttpRequests(auth -> auth
                // 1. Rutas Públicas
                .requestMatchers("/", "/search", "/search/**",
                        "/login", "/register",
                        "/css/**", "/js/**", "/images/**").permitAll()
                
                // 2. Rutas API Públicas
                .requestMatchers("/api/auth/**").permitAll()
                
                // 3. API Protegida
                .requestMatchers("/api/**").authenticated()
                
                // 4. Vistas Generales Protegidas
                .requestMatchers("/recipe/**", "/dashboard", "/machinery/{id}").authenticated()
                
                // 5. ZONA DUEÑO (Gestión de maquinaria)
                .requestMatchers(
                    "/admin/**", 
                    "/machinery/new", 
                    "/machinery/edit/**", 
                    "/machinery/save", 
                    "/machinery/delete/**"
                ).hasRole("DUENO") 
                
                // 6. Resto bloqueado
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       PasswordEncoder passwordEncoder,
                                                       UserDetailsService userDetailsService) throws Exception {

        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        @SuppressWarnings("unchecked")
        DaoAuthenticationConfigurer<AuthenticationManagerBuilder, UserDetailsService> configurer =
                authBuilder.userDetailsService(userDetailsService);
        
        configurer.passwordEncoder(passwordEncoder);

        return authBuilder.build();
    }
}