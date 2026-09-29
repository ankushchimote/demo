package com.example.demo.security;

import com.example.demo.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collections;

/**
 * JWT Authentication Filter.
 * Runs ONCE per HTTP request. It looks for a JWT in the "Authorization" header,
 * verifies it, and — if valid — marks the request as authenticated in Spring Security.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // The secret key used to verify the JWT's signature (injected via constructor).
    private final SecretKey secretKey;

    public JwtAuthenticationFilter(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Read the Authorization header (expected format: "Bearer <token>").
        String authHeader = request.getHeader("Authorization");

        // 2. If there is no header or it doesn't use the "Bearer " scheme,
        //    skip authentication and continue the filter chain (request stays anonymous).
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Strip the "Bearer " prefix (7 characters) to get the raw token.
        String token = authHeader.substring(7);

        try {
            // 4. Parse and VERIFY the token using the secret key.
            //    This checks the signature and throws if the token is invalid or expired.
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)      // validate signature with secret key
                    .build()
                    .parseSignedClaims(token)   // decode + validate the token
                    .getPayload();              // get the payload (claims) inside

            String tokenType = claims.get("type", String.class);

            // Only access tokens can authenticate API requests
            if (!"access".equals(tokenType)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 5. Extract the subject (usually the username) from the claims.
            String username = claims.getSubject();
            String role = claims.get("role", String.class);
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

            // 6. Create an authentication object.
            //    Parameters: principal (username), credentials (null), authorities (null = no roles).
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.singletonList(authority)
                    );

            // 7. Register the user as authenticated for THIS request only.
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

        } catch (Exception ex) {
            // 8. Token is invalid or expired → clear any authentication so the request
            //    remains anonymous. (Spring Security will reject it later if needed.)
            SecurityContextHolder.clearContext();
        }

        // 9. Continue the filter chain regardless of success or failure.
        filterChain.doFilter(request, response);
    }
}


//access token contains:
//        {
//        "sub": "Rahul",
//        "role": "ADMIN",
//        "type": "access"
//        }
//refresh token contains:
//{
//        "sub": "Rahul",
//        "jti": "...",
//        "type": "refresh"
//        }