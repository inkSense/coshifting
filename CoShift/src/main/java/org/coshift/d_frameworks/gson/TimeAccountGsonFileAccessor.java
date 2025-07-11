package org.coshift.d_frameworks.gson;

import org.coshift.c_adapters.ports.TimeAccountJsonFileAccessor;
import org.coshift.c_adapters.dto.TimeAccountDto;
import org.springframework.stereotype.Repository;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class TimeAccountGsonFileAccessor implements TimeAccountJsonFileAccessor {

    private static final Logger LOG = LoggerFactory.getLogger(TimeAccountGsonFileAccessor.class);

    private Path file;
    private static final Type LIST_TYPE = new TypeToken<List<TimeAccountDto>>(){}.getType();

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public TimeAccountGsonFileAccessor() {
        this.file = Paths.get("data/persons", "timeAccounts.json");
    }

    public TimeAccountGsonFileAccessor(Path file) {
        this.file = file;
    }

    @Override
    public List<TimeAccountDto> readAll() {
        if (!Files.exists(file)) return Collections.emptyList();
        try (var reader = Files.newBufferedReader(file)) {
            List<TimeAccountDto> list = gson.fromJson(reader, LIST_TYPE);
            return list != null ? list : Collections.emptyList();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read timeAccounts JSON file", e);
        }
    }

    @Override
    public boolean writeAll(List<TimeAccountDto> accs) {
        try {
            Files.createDirectories(file.getParent());
            try (var writer = Files.newBufferedWriter(file)) {
                gson.toJson(accs, LIST_TYPE, writer);
            }
            return true;
        } catch (IOException e) {
            LOG.error("Unable to write timeAccounts JSON file: {}", e.getMessage());
            return false;
        }
    }
}