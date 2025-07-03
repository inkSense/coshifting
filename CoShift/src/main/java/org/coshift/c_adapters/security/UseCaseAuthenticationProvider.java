package org.coshift.c_adapters.security;

import org.coshift.b_application.UseCaseInteractor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.coshift.a_domain.person.Person;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class UseCaseAuthenticationProvider implements AuthenticationProvider {

    private final UseCaseInteractor useCaseInteractor;
    private static final Logger LOG = LoggerFactory.getLogger(UseCaseAuthenticationProvider.class);

    public UseCaseAuthenticationProvider(UseCaseInteractor useCaseInteractor) {
        this.useCaseInteractor = useCaseInteractor;
    }


    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String nickname = authentication.getName();
        String password = authentication.getCredentials().toString();
        try {
            LOG.debug("Authenticate attempt for {}", nickname);
            Person person = useCaseInteractor.authenticateUser(nickname, password);
            if (person == null) {                         // <── Mock kann null liefern
                throw new BadCredentialsException("Invalid username or password");
            }
            List<SimpleGrantedAuthority> auth =
                    List.of(new SimpleGrantedAuthority("ROLE_" + person.getRole()));
            return new UsernamePasswordAuthenticationToken(person, null, auth);
        } catch (IllegalArgumentException ex) {
            throw new BadCredentialsException("Invalid username or password", ex);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
