package org.coshift.b_application.useCases;

import org.coshift.a_domain.person.Person;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.b_application.ports.PasswordChecker;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AuthenticateUserUseCase {
    private final PersonRepository personRepo;
    private final PasswordChecker  checker;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticateUserUseCase.class);

    public AuthenticateUserUseCase(PersonRepository repo, PasswordChecker checker){
        this.personRepo = Objects.requireNonNull(repo);
        this.checker    = Objects.requireNonNull(checker);
    }

    public Person authenticate(String nick, String rawPw){
        Person p = personRepo.findByNickname(nick)
                             .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!checker.matches(rawPw, p.getPassword())) {
            LOGGER.warn("Bad credentials for user {}", nick);
            throw new IllegalArgumentException("Bad credentials");
        }
        return p;
    }
}