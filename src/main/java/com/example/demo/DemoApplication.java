package com.example.demo;

///this project is for knowledge purpose. to convert it into prod app, see the comment on the last line of file
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}


///to open swagger: http://localhost:8080/swagger-ui/index.html#/


///its not possible to test cors without frontend so we aint testing


//HTTPS and JWT solve different problems.
//
//HTTPS → encrypts data while traveling between client and server.
//		JWT → authenticates/authorizes the user.
//Refresh token rotation → controls long-lived authentication sessions.
//Spring Security → enforces access to protected endpoints.
//So your API now has the major pieces we've been building: CRUD → validation → exception handling → JWT → refresh tokens → role authorization → CORS → HTTPS.


//REGISTER
//   ↓
//BCrypt password hash
//   ↓
//DB
//
//
//		LOGIN
//   ↓
//Validate username/password
//   ↓
//Access Token (15 min)
//+
//Refresh Token (7 days)
//   ↓
//Client
//
//
//API REQUEST
//   ↓
//Authorization: Bearer <access-token>
//		↓
//JWT Filter
//   ↓
//Validate signature + expiration + token type
//   ↓
//Extract username + role
//   ↓
//Spring SecurityContext
//   ↓
//@PreAuthorize
//   ↓
//Controller
//
//
//ACCESS TOKEN EXPIRES
//   ↓
//POST /auth/refresh
//   ↓
//Validate refresh token
//   ↓
//Revoke old refresh token
//   ↓
//Generate new access + refresh tokens



//A production system would additionally consider things like:
//
//Secrets in environment variables / AWS Secrets Manager rather than YAML
//Secure refresh-token storage, often HttpOnly + Secure cookies
//Rate limiting / login throttling
//Account lockout or abuse protection
//Token/session revocation strategy at scale
//Proper certificate management
//Security headers
//Audit logging
//Dependency/security scanning
//Database encryption and credential management
//HTTPS enforcement behind a reverse proxy/load balancer
