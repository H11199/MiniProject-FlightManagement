package com.cts.pod4.authservice.controller;

import com.cts.pod4.authservice.config.JwtTokenUtil;
import com.cts.pod4.authservice.exceptions.AuthorizationException;
import com.cts.pod4.authservice.model.JwtRequest;
import com.cts.pod4.authservice.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    private Authentication authenticate(String username, String password) throws AuthorizationException {
        try {
            System.out.println("Authenticating user....");
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            System.out.println("Authentication successful...");
            System.out.println(auth.getCredentials());
            return auth;
        } catch (DisabledException e) {
            throw new AuthorizationException("USER_DISABLED");
        } catch (BadCredentialsException e) {
            throw new AuthorizationException("INVALID_CREDENTIALS");
        }
    }

    @PostMapping("/authenticate")
    public String createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws AuthorizationException {
        Authentication auth = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        System.out.println(userDetails);
        final String token = jwtTokenUtil.generateToken(userDetails);
        return token;
    }

    @PostMapping("/authorize")
    public boolean authorizeTheRequest(@RequestHeader(value = "Authorization", required = true) String requestTokenHeader) {
        System.out.println("Authorizing the user....");
        String jwtToken = null;
        String username = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            System.out.println("JWT Token: " + jwtToken);

            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException | ExpiredJwtException e ) {
                return false;
            }
        }
        return username != null;
    }

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("auth-Ok", HttpStatus.OK);
    }
}
