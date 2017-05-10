package com.intive.patronage.toz.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.intive.patronage.toz.base.model.Identifiable;
import com.intive.patronage.toz.base.model.IdentifiableView;
import com.intive.patronage.toz.users.model.db.RoleEntity;
import com.intive.patronage.toz.users.model.db.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModelMapper {

    private static final ObjectMapper OBJECT_MAPPER_ENABLED_ANNOTATIONS = new ObjectMapper();
    private static final ObjectMapper OBJECT_MAPPER_DISABLED_ANNOTATIONS = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addSerializer(RoleEntity.class, new RolesSerializer(RoleEntity.class));
        OBJECT_MAPPER_DISABLED_ANNOTATIONS
                .registerModule(module)
                .disable(MapperFeature.USE_ANNOTATIONS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

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

    public static String convertToJsonString(Object value) {
        try {
            return OBJECT_MAPPER_DISABLED_ANNOTATIONS.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private static class RolesSerializer extends StdSerializer<RoleEntity> {

        RolesSerializer(Class<RoleEntity> t) {
            super(t);
        }

        @Override
        public void serialize(RoleEntity entity, JsonGenerator gen, SerializerProvider provider) throws IOException {
            final User.Role role = entity.getRole();
            gen.writeString(role.toString());
        }
    }
}
