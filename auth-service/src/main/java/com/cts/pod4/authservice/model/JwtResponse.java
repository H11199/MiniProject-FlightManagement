package com.cts.pod4.authservice.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Getter
public class JwtResponse implements Serializable {
    private static final long serialVersionUID = 8091879091924046844L;

    private final String jwtToken;

    @Autowired
    public JwtResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
