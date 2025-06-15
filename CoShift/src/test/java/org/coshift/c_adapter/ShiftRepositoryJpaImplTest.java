package org.coshift.c_adapter;

import org.coshift.a_domain.Shift;
import org.coshift.b_application.ShiftRepository;
import org.coshift.c_adapter.persistence.ShiftMapper;
import org.coshift.c_adapter.persistence.ShiftRepositoryJpaImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ShiftMapper.class, ShiftRepositoryJpaImpl.class})
@TestPropertySource(properties = {
                "spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=coshift;encrypt=true;trustServerCertificate=true",
                "spring.datasource.username=coshift_user",
                "spring.datasource.password=myPassword",
                "spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver",
                "spring.jpa.hibernate.ddl-auto=update",
                "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect"
})

class ShiftRepositoryJpaImplTest {

    @Autowired ShiftRepository repo;

    @Test
    void saveAndLoad() {
        Shift s = new Shift(null, LocalDate.of(2025, 6, 15),
                LocalTime.of(18, 0), LocalTime.of(20, 0));

        Shift saved = repo.save(s);
        assertTrue(repo.findById(saved.getId()).isPresent());
    }
}
