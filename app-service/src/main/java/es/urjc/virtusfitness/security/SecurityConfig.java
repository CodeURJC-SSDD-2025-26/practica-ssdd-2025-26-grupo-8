package es.urjc.virtusfitness.security;

import es.urjc.virtusfitness.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  public SecurityConfig(
      UserService userService,
      PasswordEncoder passwordEncoder,
      JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }

  /** REST API chain — JWT Bearer auth, CSRF disabled, stateless. Higher priority than web chain. */
  @Bean
  @Order(1)
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/api/v1/**")
        .authenticationProvider(authenticationProvider())
        .authorizeHttpRequests(
            auth ->
                auth
                    // Public: login and signup
                    .requestMatchers(HttpMethod.POST, "/api/v1/auth/login")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/users")
                    .permitAll()
                    // Public: browse classes and their reviews
                    .requestMatchers(HttpMethod.GET, "/api/v1/classes", "/api/v1/classes/*")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/classes/*/reviews")
                    .permitAll()
                    // Public: stats for charts
                    .requestMatchers(HttpMethod.GET, "/api/v1/stats/**")
                    .permitAll()
                    // Public: images
                    .requestMatchers(HttpMethod.GET, "/api/v1/images/**")
                    .permitAll()
                    // Admin-only operations
                    .requestMatchers(HttpMethod.GET, "/api/v1/users")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/v1/classes")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/classes/*")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/classes/*")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/users/*")
                    .hasRole("ADMIN")
                    // Everything else requires authentication
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .csrf(csrf -> csrf.disable())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(
            ex ->
                ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                    .accessDeniedHandler(
                        (req, resp, exc) -> resp.setStatus(HttpStatus.FORBIDDEN.value())));
    return http.build();
  }

  /** Web chain — form login, CSRF enabled, session-based. */
  @Bean
  public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
    http
        .authenticationProvider(authenticationProvider())
        .authorizeHttpRequests(
            auth ->
                auth
                    .requestMatchers("/admin/**")
                    .hasRole("ADMIN")
                    .requestMatchers("/profile/**")
                    .authenticated()
                    .requestMatchers("/bookings/**")
                    .authenticated()
                    .requestMatchers("/classes/*/reviews")
                    .authenticated()
                    .requestMatchers("/classes/*/reviews/*/delete")
                    .authenticated()
                    .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**")
                    .permitAll()
                    .requestMatchers(
                        "/",
                        "/classes",
                        "/classes/*",
                        "/about",
                        "/contact",
                        "/pricing",
                        "/schedule",
                        "/login",
                        "/register",
                        "/error/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/v3/api-docs",
                        "/v3/api-docs.yaml")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(
            form ->
                form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/login?error=true")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .permitAll())
        .logout(
            logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll())
        .requiresChannel(channel -> channel.anyRequest().requiresSecure())
        .csrf(csrf -> csrf.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()));
    return http.build();
  }
}
