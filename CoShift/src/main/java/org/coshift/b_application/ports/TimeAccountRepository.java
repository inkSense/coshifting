package org.coshift.b_application.ports;

import org.coshift.a_domain.time.TimeAccount;
import java.util.*;

public interface TimeAccountRepository {
    TimeAccount save(TimeAccount acc);
    Optional<TimeAccount> findById(Long id);
    List<TimeAccount> findAll();
    void deleteById(Long id);
}