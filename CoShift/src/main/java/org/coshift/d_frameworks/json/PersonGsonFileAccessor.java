package org.coshift.d_frameworks.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.coshift.c_adapters.PersonDto;
import org.coshift.c_adapters.json.PersonJsonFileAccessor;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;

/**
 * Technischer Datei-Zugriff auf den Personen-Bestand.
 * Keine Geschäftslogik – diese steckt im Adapter/Use-Case.
 */
@Repository
public class PersonGsonFileAccessor implements PersonJsonFileAccessor {

    /* ------------------ Pfade & Typen ----------------------------- */

    private static final Path FILE      = Paths.get("CoShift/data/persons", "persons.json");
    private static final Type LIST_TYPE = new TypeToken<List<PersonDto>>() {}.getType();

    private static final Logger LOG = LoggerFactory.getLogger(PersonGsonFileAccessor.class);

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /* -------------------- Lesen ----------------------------------- */

    @Override
    public List<PersonDto> readAll() {
        if (!Files.exists(FILE)) {
            return Collections.emptyList();
        }
        try (var reader = Files.newBufferedReader(FILE)) {
            List<PersonDto> list = gson.fromJson(reader, LIST_TYPE);
            return list != null ? list : Collections.emptyList();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read persons JSON file", e);
        }
    }

    /* -------------------- Schreiben ------------------------------- */

    @Override
    public boolean writeAll(List<PersonDto> persons) {
        try {
            Files.createDirectories(FILE.getParent());
            try (var writer = Files.newBufferedWriter(FILE)) {
                gson.toJson(persons, LIST_TYPE, writer);
            }
            return true;
        } catch (IOException e) {
            LOG.error("Unable to write persons JSON file: {}", e.getMessage());
            return false;
        }
    }
}