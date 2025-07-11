package org.coshift.c_adapters;

import org.coshift.a_domain.time.*;
import org.coshift.c_adapters.persistence.json.TimeAccountJsonRepository;
import org.coshift.d_frameworks.gson.TimeAccountGsonFileAccessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TimeAccountJsonRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(TimeAccountJsonRepositoryTest.class);

    @TempDir static Path tmp;
    Path file = tmp.resolve("timeAccounts.json");
    TimeAccountGsonFileAccessor accessor = new TimeAccountGsonFileAccessor(file);
    TimeAccountJsonRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(file);
        repository = new TimeAccountJsonRepository(accessor);
    }

    @Test
    void save_assigns_sequential_ids_starting_with_1() {
        TimeAccount a = new TimeAccount(0, new TimeBalance(0L, LocalDateTime.now()));
        TimeAccount b = new TimeAccount(0, new TimeBalance(0L, LocalDateTime.now()));

        long idA = repository.save(a).getId();
        long idB = repository.save(b).getId();

        assertThat(idA).isEqualTo(1L);
        assertThat(idB).isEqualTo(2L);
    }

    @Test
    void save_and_reload_persists_balance_and_transactions() {
        TimeAccount account = new TimeAccount(0, new TimeBalance(0L, LocalDateTime.now()));
        account.addTransaction(new TimeTransaction(
                120,                    
                LocalDateTime.now()));
        account.refreshBalance(LocalDateTime.now());

        /* ---------- WHEN ------------------------------------------ */
        TimeAccount persisted = repository.save(account);   
        TimeAccount reloaded  = repository.findById(
                                   persisted.getId()).orElseThrow();

        /* ---------- THEN ------------------------------------------ */
        assertThat(reloaded.getTransactions()).hasSize(1);
        log.info("Reloaded: {}", reloaded);
        assertThat(reloaded.getBalance().getAmountInMinutes()).isEqualTo(120);
    }

}