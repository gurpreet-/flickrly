package com.flickrly.flickrly.converters;

import com.google.gson.*;
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.reflect.Type;

public class GsonDateTimeConverter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withZone(ZoneId.of("UTC"));

    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(dateTimeFormatter.format(src));
    }

    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        // Do not try to deserialize null or empty values
        String date = json.getAsString();

        if (date == null
                || date.isEmpty()
                || date.equalsIgnoreCase("null")) {
            return null;
        }

        return Instant.parse(date);
    }
}