package org.coshift.c_adapter.persistence;

import org.coshift.b_application.ShiftRepository;
import org.coshift.a_domain.Shift;
import org.coshift.d_frameworks.db.*;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository                     // vom Spring Boot Main-Modul gescannt
//@RequiredArgsConstructor      //lombok entfernt
@Transactional                  // jede Methode in einer TX
public class ShiftRepositoryJpaImpl implements ShiftRepository {

    private final SpringDataShiftJpaRepository jpa;

    public ShiftRepositoryJpaImpl(SpringDataShiftJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Shift save(Shift shift) {
        ShiftJpaEntity saved = jpa.save(ShiftMapper.toEntity(shift));
        return ShiftMapper.toDomain(saved);
    }

    @Override
    public Optional<Shift> findById(Long id) {
        return jpa.findById(id).map(ShiftMapper::toDomain);
    }

    @Override
    public List<Shift> findAll() {
        return jpa.findAll().stream().map(ShiftMapper::toDomain).toList();
    }

    @Override
    public List<Shift> findByDate(LocalDate date) {
        return jpa.findByDate(date).stream().map(ShiftMapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }
}

