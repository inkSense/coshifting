package org.coshift.d_frameworks.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SpringDataShiftJpaRepository
        extends JpaRepository<ShiftJpaEntity, Long> {

    List<ShiftJpaEntity> findByDate(LocalDate date);
}

