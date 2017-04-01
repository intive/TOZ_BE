package com.intive.patronage.toz.util;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.base.model.Identifiable;
import com.intive.patronage.toz.base.model.IdentifiableView;

import java.util.ArrayList;
import java.util.List;

public class ModelMapper {

    private static final ObjectMapper OBJECT_MAPPER_ENABLED_ANNOTATIONS = new ObjectMapper();
    private static final ObjectMapper OBJECT_MAPPER_DISABLED_ANNOTATIONS =
            new ObjectMapper().disable(MapperFeature.USE_ANNOTATIONS);

    public static <T extends IdentifiableView> T convertToView(Object model, Class<T> viewClass) {
        return OBJECT_MAPPER_DISABLED_ANNOTATIONS.convertValue(model, viewClass);
    }

    public static <T extends IdentifiableView> List<T> convertToView(List<? extends Identifiable> models, Class<T> viewClass) {
        List<T> views = new ArrayList<>();
        for (Object model : models) {
            T view = convertToView(model, viewClass);
            views.add(view);
        }
        return views;
    }

    public static <T extends Identifiable> T convertToModel(Object view, Class<T> modelClass) {
        return OBJECT_MAPPER_ENABLED_ANNOTATIONS.convertValue(view, modelClass);
    }
}
