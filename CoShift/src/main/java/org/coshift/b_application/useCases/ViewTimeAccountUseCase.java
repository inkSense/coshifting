package org.coshift.b_application.useCases;

import org.coshift.b_application.ports.PersonRepository;
import org.coshift.b_application.ports.TimeAccountRepository;
import org.coshift.a_domain.time.TimeAccount;

public class ViewTimeAccountUseCase {
    private final TimeAccountRepository accounts;
    private final PersonRepository      persons;

    public ViewTimeAccountUseCase(TimeAccountRepository a,
                                  PersonRepository p) {
        this.accounts = a;
        this.persons  = p;
    }

    public TimeAccount getByPersonId(long personId) {
        long accId = persons.findById(personId)
                            .orElseThrow(() -> new IllegalArgumentException("Person not found"))
                            .getTimeAccountId();
        return accounts.findById(accId)
                       .orElseThrow(() -> new IllegalArgumentException("Time account not found"));
    }
}
