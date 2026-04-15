package es.urjc.virtusfitness.config;

import es.urjc.virtusfitness.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for VirtusFitness.
 *
 * <p>Enforces role-based URL access control, HTTPS on all requests,
 * form-based login/logout, and CSRF protection (enabled by default).</p>
 *
 * <ul>
 *   <li>/admin/** — ROLE_ADMIN only</li>
 *   <li>/profile/**, /bookings/**, /classes/*&#47;reviews — authenticated users</li>
 *   <li>Everything else — public</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            // URL-based role access control
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/profile/**").authenticated()
                .requestMatchers("/bookings/**").authenticated()
                .requestMatchers("/classes/*/reviews").authenticated()
                .requestMatchers("/classes/*/reviews/*/delete").authenticated()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()
                .requestMatchers("/", "/classes", "/classes/*", "/about", "/contact",
                        "/pricing", "/schedule", "/login", "/register", "/error/**").permitAll()
                .anyRequest().authenticated()
            )
            // Form-based login — Spring Security handles CSRF token automatically
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // Force HTTPS on all requests
            .requiresChannel(channel -> channel
                .anyRequest().requiresSecure()
            );
        // CSRF protection is ENABLED by default; Thymeleaf injects tokens automatically

        return http.build();
    }
}
