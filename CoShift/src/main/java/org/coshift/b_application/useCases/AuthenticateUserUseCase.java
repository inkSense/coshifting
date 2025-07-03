package org.coshift.b_application.useCases;

import org.coshift.a_domain.person.Person;
import org.coshift.b_application.ports.PersonRepository;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Use-Case: Prüft Benutzer-Anmeldedaten.
 *
 * • Primärer Treiber: z. B. ein Spring-Security-Adapter oder ein REST-Controller.  
 * • Primärer Port  : die Methode {@link #authenticate(String, String)}.  
 * • Sekundäre Ports: {@link PersonRepository}.
 *
 * Ergebnis: Gibt bei Erfolg das {@link Person}-Objekt zurück, andernfalls
 * wirft er eine {@link IllegalArgumentException}.
 *
 * Hinweise / Erweiterungspunkte:
 * – Für Passwort-Hashing kann statt des simplen String-Vergleichs eine
 *   Strategie-Schnittstelle (z. B. PasswordChecker) injiziert werden.
 * – Rollen sind bereits Bestandteil von {@code PersonRole}; der Aufrufer
 *   kann diese für Autorisierungsentscheidungen nutzen.
 */
public class AuthenticateUserUseCase {

    private final PersonRepository personRepo;
    private final PasswordEncoder  encoder;
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticateUserUseCase.class);

    //ToDo: Diesen Konstruktor löschen
    public AuthenticateUserUseCase(PersonRepository personRepo) {
        this.personRepo = Objects.requireNonNull(personRepo);
        this.encoder = null;
    }

    

    public AuthenticateUserUseCase(PersonRepository personRepo,
                                   PasswordEncoder encoder) {
        this.personRepo = Objects.requireNonNull(personRepo);
        this.encoder = Objects.requireNonNull(encoder);
    }



    /**
     * Authentifiziert einen Benutzer anhand von Spitzname und Passwort.
     *
     * @param nickname der in der Community eindeutige Spitzname
     * @param password Klartext-Passwort (oder schon gehashter Wert, sobald
     *                 Hash-Migration erfolgt)
     * @return die zugehörige {@link Person}
     * @throws IllegalArgumentException wenn der Benutzer nicht existiert
     *                                  oder das Passwort nicht stimmt
     */

    // ToDo: Diese Methode löschen und unten einkommentieren
    public Person authenticate(String nick, String pw) {
        LOG.debug("Authenticate attempt for {}", nick);
        Person p = personRepo.findByNickname(nick)
                       .orElseThrow(() -> {
                           LOG.info("Nickname {} not found", nick);
                           return new IllegalArgumentException("User not found");
                       });
    
        if (!Objects.equals(p.getPassword(), pw)) {
            LOG.info("Password mismatch for {}", nick);
            throw new IllegalArgumentException("Bad credentials");
        }
        LOG.debug("User {} authenticated", nick);
        return p;
    }


    // ToDo: Diese Methode einkommentieren und oben löschen
    // public Person authenticate(String nickname, String rawPassword) {
    //     Person person = personRepo.findByNickname(nickname)
    //             .orElseThrow(() -> {
    //     LOG.info("Nickname {} not found", nickname);
    //         return new IllegalArgumentException("User not found");
    //     });
    //     if (!encoder.matches(rawPassword, person.getPassword())) {
    //      LOG.info("Password mismatch for {}", nickname);
    //         throw new IllegalArgumentException("Bad credentials");
    //     }
    //     LOG.debug("User {} authenticated", nickname);
    //     return person;
    // }
}