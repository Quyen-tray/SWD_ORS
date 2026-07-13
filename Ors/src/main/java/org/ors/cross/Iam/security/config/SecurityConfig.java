package org.ors.cross.Iam.security.config;

import org.ors.cross.Iam.security.user.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

// TODO: thêm @EnableWebSecurity @RequiredArgsConstructor (inject JwtAuthenticationFilter).
//   Beans: PasswordEncoder(BCrypt), AuthenticationManager(từ AuthenticationConfiguration), CorsConfigurationSource(origin http://localhost:5173).
//   SecurityFilterChain: cors() + csrf(disable) + sessionManagement(STATELESS)
//     + addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//   authorizeHttpRequests (path SAU context-path /api — KHÔNG thêm /api):
//     permitAll: OPTIONS /**, /auth/**, GET /products/**, GET /categories/**, /swagger-ui/**, /swagger-ui.html, /v3/api-docs/**, /actuator/health
//     hasRole("CUSTOMER"): /carts/**
//     hasAnyRole("ADMIN","STAFF"): POST/PUT/DELETE products+categories, PUT /orders/*/status
//     anyRequest().authenticated()
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf -> csrf.disable())) //Tắt csrf vì web restApi ko cần
                .cors(cors ->cors.configurationSource(corsConfigurationSource())) //bật customs config cors có thể viết là Customizer.withDefaults()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Vì dùng jwt nên tắt session
                .authorizeHttpRequests((auth)->{
                    auth
                            .requestMatchers("/","/api/auth/**").permitAll()
                            .anyRequest().permitAll(); //đang mở tất cả Api ko cần check
                });

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        // 5173 là cổng mặc định của Vite (ors-frontend chạy bằng Vite, không phải CRA),
        // nên nếu chỉ để 3000 thì mọi request từ frontend đều bị CORS chặn.
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000"));
        configuration.setAllowCredentials(true);//cho phép mọi request dính kèm cookie
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Spring Security 7 (đi kèm Boot 4.x) bỏ setUserDetailsService(): UserDetailsService
        // phải truyền qua constructor.
        DaoAuthenticationProvider daoAuthenticationProvider =
                new DaoAuthenticationProvider(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        // Provider phải được đưa VÀO ProviderManager. new ProviderManager() rỗng thì không
        // có provider nào, nên mọi lần xác thực đều ném ProviderNotFoundException.
        return new ProviderManager(daoAuthenticationProvider);
    }
}
