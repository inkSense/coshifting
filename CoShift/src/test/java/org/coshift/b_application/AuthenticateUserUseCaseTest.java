package org.coshift.b_application;

import org.coshift.a_domain.person.Person;
import org.coshift.b_application.ports.PasswordChecker;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.b_application.useCases.AuthenticateUserUseCase;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit-Tests für {@link AuthenticateUserUseCase}.
 */
class AuthenticateUserUseCaseTest {

    private PasswordChecker trueChecker() {
        return new PasswordChecker() {
            @Override public boolean matches(String raw, String hashed) {
                return raw.equals(hashed);
            }
            @Override public String hash(String raw) {            // wird im Test gar nicht benutzt
                return raw;
            }
        };
    }

    @Test
    void authenticate_validCredentials_returnsPerson() {
        PersonRepository repo = mock(PersonRepository.class);
        PasswordChecker  checker = trueChecker();

        AuthenticateUserUseCase useCase =
                new AuthenticateUserUseCase(repo, checker);

        String nick = "anton";
        String pw   = "secret";

        Person persisted = new Person(1L, nick, pw);
        when(repo.findByNickname(nick)).thenReturn(Optional.of(persisted));

        // Act ----------------------------------------------------------
        Person result = useCase.authenticate(nick, pw);

        // Assert -------------------------------------------------------
        verify(repo).findByNickname(nick);
        assertSame(persisted, result);      // exakt dasselbe Objekt
    }

    @Test
    void authenticate_wrongPassword_throwsException() {
        PersonRepository repo = mock(PersonRepository.class);
        PasswordChecker  checker = trueChecker();

        AuthenticateUserUseCase useCase =
                new AuthenticateUserUseCase(repo, checker);

        String nick = "berta";
        String pw   = "wrong";

        Person persisted = new Person(2L, nick, "correct");
        when(repo.findByNickname(nick)).thenReturn(Optional.of(persisted));

        // Act & Assert -------------------------------------------------
        assertThrows(IllegalArgumentException.class,
                     () -> useCase.authenticate(nick, pw));

        verify(repo).findByNickname(nick);
    }

    @Test
    void authenticate_userNotFound_throwsException() {
        // Arrange ------------------------------------------------------
        PersonRepository repo = mock(PersonRepository.class);
        PasswordChecker  checker = trueChecker();

        AuthenticateUserUseCase useCase =
                new AuthenticateUserUseCase(repo, checker);

        String nick = "charlie";
        String pw   = "secret";

        when(repo.findByNickname(nick)).thenReturn(Optional.empty());

        // Act & Assert -------------------------------------------------
        assertThrows(IllegalArgumentException.class,
                     () -> useCase.authenticate(nick, pw));

        verify(repo).findByNickname(nick);
    }
}