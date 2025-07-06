package org.coshift.b_application.ports;

public interface PasswordChecker {
    boolean matches(String rawPassword, String hashedPassword);
    String   hash(String rawPassword);           // optional, für Registrierungs-Use-Case
}
