package attestation_finalProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")) // REST без CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/error",
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                                "/actuator/**", "/static/**").permitAll()
                        .requestMatchers("/api/**").authenticated() // API защищено
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults()) // удобно для Postman/Swagger
                .formLogin(form -> form.loginPage("/login").permitAll())
                .logout(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin").password(encoder.encode("admin123")).roles("ADMIN").build(),
                User.withUsername("user").password(encoder.encode("user123")).roles("USER").build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
