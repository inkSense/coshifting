package org.coshift.c_adapters;

import org.coshift.a_domain.person.Person;
import org.coshift.a_domain.person.PersonRole;
import org.coshift.b_application.UseCaseInteractor;
import org.coshift.c_adapters.security.UseCaseAuthenticationProvider;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit-Tests für {@link UseCaseAuthenticationProvider}.
 *
 *  – Reiner JUnit- / Mockito-Test, ohne Spring-Context-Start.  
 *  – Prüft Erfolgsfall und Fehlerfälle (falsches Passwort / unbekannter User).  
 */
class UseCaseAuthenticationProviderTest {

    @Test
    void authenticate_validCredentials_returnsAuthenticatedToken() {
        /* ---------- Arrange ---------------------------------------- */
        UseCaseInteractor interactor = mock(UseCaseInteractor.class);
        UseCaseAuthenticationProvider provider = new UseCaseAuthenticationProvider(interactor);

        String nick = "anton";
        String pw   = "secret";

        Person anton = new Person(1L, nick, pw, PersonRole.USER);

        when(interactor.authenticateUser(nick, pw)).thenReturn(anton);

        Authentication request = new UsernamePasswordAuthenticationToken(nick, pw);

        /* ---------- Act -------------------------------------------- */
        Authentication result = provider.authenticate(request);

        /* ---------- Assert ----------------------------------------- */
        assertTrue(result.isAuthenticated());
        assertEquals(anton, result.getPrincipal());
        assertEquals(List.of("ROLE_USER"),
                     result.getAuthorities().stream()
                           .map(a -> a.getAuthority()).toList());

        verify(interactor).authenticateUser(nick, pw);
    }

    @Test
    void authenticate_wrongPassword_throwsBadCredentials() {
        /* ---------- Arrange ---------------------------------------- */
        UseCaseInteractor interactor = mock(UseCaseInteractor.class);
        UseCaseAuthenticationProvider provider = new UseCaseAuthenticationProvider(interactor);

        String nick = "berta";
        String pw   = "wrong";

        when(interactor.authenticateUser(nick, pw))
                .thenThrow(new IllegalArgumentException("Invalid credentials"));

        Authentication request = new UsernamePasswordAuthenticationToken(nick, pw);

        /* ---------- Act & Assert ----------------------------------- */
        assertThrows(BadCredentialsException.class,
                     () -> provider.authenticate(request));

        verify(interactor).authenticateUser(nick, pw);
    }

    @Test
    void supports_usernamePasswordToken_returnsTrue() {
        UseCaseAuthenticationProvider provider =
                new UseCaseAuthenticationProvider(mock(UseCaseInteractor.class));

        assertTrue(provider.supports(UsernamePasswordAuthenticationToken.class));
    }
}