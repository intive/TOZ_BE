package com.intive.patronage.toz.model;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.model.db.Identifiable;
import com.intive.patronage.toz.model.db.IdentifiableView;

public class ModelMapper {

    private static final ObjectMapper OBJECT_MAPPER_ENABLED_ANNOTATIONS = new ObjectMapper();
    private static final ObjectMapper OBJECT_MAPPER_DISABLED_ANNOTATIONS =
            new ObjectMapper().disable(MapperFeature.USE_ANNOTATIONS);

    public static <T extends IdentifiableView> T convertToView(Object model, Class<T> viewClass) {
        return OBJECT_MAPPER_DISABLED_ANNOTATIONS.convertValue(model, viewClass);
    }

    public static <T extends Identifiable> T convertToModel(Object view, Class<T> modelClass) {
        return OBJECT_MAPPER_ENABLED_ANNOTATIONS.convertValue(view, modelClass);
    }
}
