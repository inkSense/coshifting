package org.coshift.d_frameworks.gson;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Gson-Adapter für java.time.LocalDateTime – Serialisierung als ISO-8601-String.
 */
public final class LocalDateTimeAdapter
        implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public JsonElement serialize(LocalDateTime src, Type t, JsonSerializationContext c) {
        return new JsonPrimitive(ISO.format(src));
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type t, JsonDeserializationContext c)
            throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(), ISO);
    }
}