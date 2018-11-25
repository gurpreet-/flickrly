/*
 * Copyright (c) Terl Tech Ltd • 16/07/18 13:02 • goterl.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.flickrly.flickrly.converters;

import com.google.gson.*;
import org.threeten.bp.LocalDate;

import java.lang.reflect.Type;

public class LocalDateConverter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {


    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        // Do not try to deserialize null or empty values
        String got = json.getAsString();
        return LocalDate.parse(got);
    }
}