package org.coshift.c_adapters.security;

import org.coshift.b_application.ports.PasswordChecker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SpringPasswordChecker implements PasswordChecker {
    private final PasswordEncoder encoder;

    SpringPasswordChecker(PasswordEncoder encoder){ 
        this.encoder = encoder; 
    }

    @Override public boolean matches(String raw, String hash){ 
        return encoder.matches(raw, hash); 
    }
    
    @Override public String   hash(String raw){ 
        return encoder.encode(raw); 
    }
}
