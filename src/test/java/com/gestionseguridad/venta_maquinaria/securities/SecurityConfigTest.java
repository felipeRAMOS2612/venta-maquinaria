package com.gestionseguridad.venta_maquinaria.securities;

import com.gestionseguridad.venta_maquinaria.models.User;
import com.gestionseguridad.venta_maquinaria.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Mock
    private HttpSecurity httpSecurity;
    @Mock
    private AuthenticationManagerBuilder authBuilder;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManagerMock;

    @Test
    void testUserDetailsService_UserFound() {
        String email = "admin@test.com";
        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword("pass");
        mockUser.setRol(User.Rol.DUENO); 

        when(userService.getUserByEmail(email)).thenReturn(mockUser);

        UserDetailsService userDetailsService = securityConfig.userDetailsService();
        UserDetails result = userDetailsService.loadUserByUsername(email);

        assertNotNull(result);
        assertEquals(email, result.getUsername());
    }

    @Test
    void testUserDetailsService_UserNotFound() {
        String email = "noexiste@test.com";
        when(userService.getUserByEmail(email)).thenReturn(null);

        UserDetailsService userDetailsService = securityConfig.userDetailsService();

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });
    }

    @Test
    void testAuthenticationManager() throws Exception {
        when(httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)).thenReturn(authBuilder);

        @SuppressWarnings("unchecked")
        DaoAuthenticationConfigurer<AuthenticationManagerBuilder, UserDetailsService> daoConfigurer = 
                mock(DaoAuthenticationConfigurer.class);

        doReturn(daoConfigurer).when(authBuilder).userDetailsService(any(UserDetailsService.class));
        doReturn(daoConfigurer).when(daoConfigurer).passwordEncoder(any(PasswordEncoder.class));
        
        when(authBuilder.build()).thenReturn(authenticationManagerMock);

        UserDetailsService dummyService = mock(UserDetailsService.class);

        AuthenticationManager result = securityConfig.authenticationManager(httpSecurity, passwordEncoder, dummyService);

        assertNotNull(result);
        verify(authBuilder).build();
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void testFilterChain() throws Exception {
        CsrfConfigurer<HttpSecurity> csrfConfigurer = mock(CsrfConfigurer.class);
        FormLoginConfigurer<HttpSecurity> formLoginConfigurer = mock(FormLoginConfigurer.class);
        LogoutConfigurer<HttpSecurity> logoutConfigurer = mock(LogoutConfigurer.class);
        
        AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry authRegistry = 
            mock(AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry.class);
        AuthorizeHttpRequestsConfigurer.AuthorizedUrl authorizedUrl = 
            mock(AuthorizeHttpRequestsConfigurer.AuthorizedUrl.class);

        when(httpSecurity.csrf(any())).thenAnswer(invocation -> {
            Customizer<CsrfConfigurer<HttpSecurity>> customizer = invocation.getArgument(0);
            customizer.customize(csrfConfigurer);
            return httpSecurity;
        });

        when(httpSecurity.authorizeHttpRequests(any())).thenAnswer(invocation -> {
            Customizer<AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry> customizer = invocation.getArgument(0);
            customizer.customize(authRegistry);
            return httpSecurity;
        });

        when(httpSecurity.formLogin(any())).thenAnswer(invocation -> {
            Customizer<FormLoginConfigurer<HttpSecurity>> customizer = invocation.getArgument(0);
            customizer.customize(formLoginConfigurer); 
            return httpSecurity;
        });

        when(httpSecurity.logout(any())).thenAnswer(invocation -> {
            Customizer<LogoutConfigurer<HttpSecurity>> customizer = invocation.getArgument(0);
            customizer.customize(logoutConfigurer); 
            return httpSecurity;
        });

        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        
        // CORRECCIÓN PRINCIPAL: Devuelve DefaultSecurityFilterChain
        when(httpSecurity.build()).thenReturn(mock(DefaultSecurityFilterChain.class));

        when(csrfConfigurer.ignoringRequestMatchers(anyString())).thenReturn(csrfConfigurer);

        when(authRegistry.requestMatchers(any(String[].class))).thenReturn(authorizedUrl);
        when(authRegistry.requestMatchers(anyString())).thenReturn(authorizedUrl);
        when(authRegistry.anyRequest()).thenReturn(authorizedUrl);
        
        when(authorizedUrl.permitAll()).thenReturn(authRegistry);
        when(authorizedUrl.authenticated()).thenReturn(authRegistry);
        when(authorizedUrl.hasRole(anyString())).thenReturn(authRegistry);

        when(formLoginConfigurer.loginPage(anyString())).thenReturn(formLoginConfigurer);
        when(formLoginConfigurer.defaultSuccessUrl(anyString(), anyBoolean())).thenReturn(formLoginConfigurer);
        when(formLoginConfigurer.failureUrl(anyString())).thenReturn(formLoginConfigurer);
        when(formLoginConfigurer.permitAll()).thenReturn(formLoginConfigurer);

        when(logoutConfigurer.logoutUrl(anyString())).thenReturn(logoutConfigurer);
        when(logoutConfigurer.logoutSuccessUrl(anyString())).thenReturn(logoutConfigurer);
        when(logoutConfigurer.permitAll()).thenReturn(logoutConfigurer);

        SecurityFilterChain result = securityConfig.filterChain(httpSecurity);

        assertNotNull(result);
        verify(httpSecurity).build();
    }
}