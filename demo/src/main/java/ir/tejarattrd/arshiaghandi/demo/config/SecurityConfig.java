package ir.tejarattrd.arshiaghandi.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF را فعلا غیرفعال می‌کنیم چون برای APIهای RESTful معمولا استفاده نمی‌شود
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // به این مسیرها همه می‌توانند دسترسی داشته باشند
                        .requestMatchers("/api/auth/**").permitAll()
                        // برای بقیه مسیرها احراز هویت لازم است
                        .anyRequest().authenticated()
                );
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}