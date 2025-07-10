package org.coshift.d_frameworks.gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.coshift.c_adapters.dto.ShiftDto;
import org.coshift.c_adapters.ports.ShiftJsonFileAccessor;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





@Repository          
public class ShiftGsonFileAccessor implements ShiftJsonFileAccessor {



    private static final Path FILE      = Paths.get("data/shifts25", "shifts.json");
    private static final Type LIST_TYPE = new TypeToken<List<ShiftDto>>() {}.getType();
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private static final Logger LOGGER = LoggerFactory.getLogger(ShiftGsonFileAccessor.class);

    @Override
    public List<ShiftDto> readAll() {
        if (!Files.exists(FILE)) {
            return Collections.emptyList();
        }
        try (var reader = Files.newBufferedReader(FILE)) {
            List<ShiftDto> list = gson.fromJson(reader, LIST_TYPE);
            return list != null ? list : Collections.emptyList();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read shifts JSON file", e);
        }
    }

    @Override
    public boolean writeAll(List<ShiftDto> shifts) {
        try {
            Files.createDirectories(FILE.getParent());
            try (var writer = Files.newBufferedWriter(FILE)) {
                gson.toJson(shifts, LIST_TYPE, writer);
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("writing didnt work");
            return false;
        }
    }
}