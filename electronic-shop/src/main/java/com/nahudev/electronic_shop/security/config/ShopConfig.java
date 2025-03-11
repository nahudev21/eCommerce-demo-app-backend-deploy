package com.nahudev.electronic_shop.security.config;

import com.nahudev.electronic_shop.security.jwt.AuthTokenFilter;
import com.nahudev.electronic_shop.security.jwt.JwtAuthEntryPoint;
import com.nahudev.electronic_shop.security.jwt.LogoutService;
import com.nahudev.electronic_shop.security.user.ShopUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class ShopConfig {

    private final JwtAuthEntryPoint authEntryPoint;

    private final ShopUserDetailsService userDetailsService;

    private static final List<String> SECURED_URLS = List.of("/api/v1/carts/**" ,
            "/api/v1/cartItems/**", "/api/v1/orders/**");

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers(SECURED_URLS.toArray(String[]::new)).authenticated()
                        .anyRequest().permitAll());
         http.authenticationProvider(daoAuthenticationProvider());
         http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
         http.logout(logout -> {
             logout.logoutUrl("/api/v1/auth/logout");
             logout.addLogoutHandler(logoutHandler());
             logout.logoutSuccessHandler((request, response, authentication) ->
                     SecurityContextHolder.clearContext()
             );
         });
        http.cors(Customizer.withDefaults());

         return http.build();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public LogoutService logoutHandler() {
        return new LogoutService();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource () {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173/"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
