package org.coshift.b_application;

import org.coshift.a_domain.person.Person;
import org.coshift.a_domain.person.PersonRole;
import org.coshift.b_application.ports.PasswordChecker;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.b_application.ports.PresenterInputPort;
import org.coshift.b_application.ports.ShiftRepository;
import org.coshift.b_application.ports.TimeAccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class UseCaseInteractorAuthorizationTest {

    UseCaseInteractor interactor;

    @BeforeEach
    void setUp() {
        ShiftRepository shiftRepo = mock(ShiftRepository.class);
        PersonRepository personRepo = mock(PersonRepository.class);
        TimeAccountRepository timeAccountRepo = mock(TimeAccountRepository.class);
        PasswordChecker passwordChecker = mock(PasswordChecker.class);
        PresenterInputPort presenter = mock(PresenterInputPort.class);
        interactor = new UseCaseInteractor(shiftRepo, personRepo, timeAccountRepo, passwordChecker, presenter);
    }

    @AfterEach
    void clearSecurity() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void userCannotModifyOtherPerson() {
        Person principal = new Person(1L, "anton", "pw", PersonRole.USER);
        var auth = new UsernamePasswordAuthenticationToken(principal, null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThrows(AccessDeniedException.class, () -> interactor.addPersonToShift(2L, 1L));
        assertThrows(AccessDeniedException.class, () -> interactor.removePersonFromShift(2L, 1L));
    }
}
