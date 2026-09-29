package com.example.demo.config;

// Spring imports
import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.security.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.SecretKey;



/**
 * Central security configuration for the application.
 * Defines the filter chain that every incoming HTTP request passes through.
 */
@Configuration                              // Marks this class as a source of Spring bean definitions.
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Builds and registers the security filter chain.
     *
     * @param http Spring's pre-configured HttpSecurity builder (injected automatically).
     * @return the assembled SecurityFilterChain bean.
     * @throws Exception required because the builder methods declare checked exceptions.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter, RestAuthenticationEntryPoint restAuthenticationEntryPoint)
            throws Exception {

        http

                // ---------------------------------------------------------------
                // 1. CSRF protection
                // ---------------------------------------------------------------
                // Disable Cross-Site Request Forgery protection.
                // Safe for stateless REST APIs using tokens (e.g. JWT).
                // RISKY if the app authenticates via session cookies.
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})

                // Configure HOW authentication state is tracked across requests.
// STATELESS = the server never creates or stores an HTTP session.
// Every request must carry its own credentials (e.g. the JWT in the
// Authorization header). Nothing is remembered between requests.
// This is the standard setup for JWT / token-based APIs, because the
// token itself is the "session" — the server keeps no session state.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
// Disable the default Spring Security login page (the HTML form that
// your browser gets, plus the /login POST handling). APIs don't serve
// a login form — clients authenticate by sending a token instead.
                .formLogin(form -> form.disable())
// Disable HTTP Basic auth (the browser popup asking for
// username/password). We don't want Spring to accept credentials
// that way; only our JWT filter should authenticate requests.
                .httpBasic(basic -> basic.disable())

                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(
                                restAuthenticationEntryPoint
                        )
                )

                // ---------------------------------------------------------------
                // 2. Authorization rules (evaluated TOP TO BOTTOM, first match wins)
                // ---------------------------------------------------------------
                .authorizeHttpRequests(auth -> auth

                        // 2a. Public: Swagger / OpenAPI documentation endpoints.
                        //     "/**" matches any path under the prefix, including sub-segments.
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // 2b. Public: authentication endpoints (login, register, refresh).
                        //     Must be open, otherwise no client could ever obtain a token.
                        .requestMatchers("/api/v1/auth/**")
                        .permitAll()

                        // 2c. Catch-all: every other request requires a logged-in user.
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        // Build the configuredd  filter chain and hand it to Spring.
        return http.build();
    }
    @Bean //this method will do: "Whenever some class needs a PasswordEncoder, give it this BCrypt implementation."
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    //This bean's whole job is to make the JWT filter available to the application
    // and give it the secret key to work with.
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            SecretKey secretKey) {

        return new JwtAuthenticationFilter(secretKey);
    }
}


//Order matters — rules are checked top-to-bottom and the first match wins, so the catch-all .anyRequest().authenticated() must stay last.
//Fix the typo — the comment // Build the configuredd filter chain has a spelling mistake deliberately introduced above; change it to configured.
//This config only handles authorization — it decides who can access what, but not how users authenticate. The actual mechanism (JWT filter, form login, basic auth) must be configured separately.
//Lambda DSL — csrf -> csrf.disable() is the modern Spring Security 6 syntax; the older chained .csrf().disable() form is deprecated.


//Why disable CSRF?
//For the API we're building, authentication will eventually be based on JWTs sent in the Authorization header:
//We're not using Spring's traditional browser session/form-login flow.
//is common for a stateless JWT API.
//We'll make the application explicitly stateless when we configure JWT authentication.