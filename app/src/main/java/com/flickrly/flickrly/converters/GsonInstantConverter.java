package com.flickrly.flickrly.converters;

import android.util.Log;
import com.google.gson.*;
import org.threeten.bp.Instant;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.reflect.Type;

public class GsonInstantConverter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(dateTimeFormatter.format(src));
    }

    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String date = json.getAsString();

        if (date == null
                || date.isEmpty()
                || date.equalsIgnoreCase("null")) {
            return null;
        }
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(date, dateTimeFormatter);
        return zonedDateTime.toInstant();
    }
}