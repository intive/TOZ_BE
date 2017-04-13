package com.intive.patronage.toz.mail;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.JsonNodeValueResolver;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.GuavaTemplateCache;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
class MailService {
    //TODO przygotować model i zserializować go do JsonNode;
    //TODO dodać obsługę wyjątku w przypadku gdy nie można wczytać Template

    public String getRegistrationTemplate(String token) throws IOException {
        Template template = getTemplate("Registration");
        return template.apply(getContext(null));
    }

    Handlebars getHandlebarsTemplates() {
        TemplateLoader loader = new ClassPathTemplateLoader("/templates", ".hbs");
        final Cache<TemplateSource, Template> templateCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).maximumSize(1000).build();
        return new Handlebars(loader).with((new GuavaTemplateCache(templateCache)));
    }

    Template getTemplate(String templateName) throws IOException {
        Handlebars handlebarsTemplates = getHandlebarsTemplates();
        return handlebarsTemplates.compile(templateName);
    }

    Context getContext(JsonNode jsonNode) {
        return Context
                .newBuilder(jsonNode)
                .resolver(JsonNodeValueResolver.INSTANCE, JavaBeanValueResolver.INSTANCE, FieldValueResolver.INSTANCE,
                        MapValueResolver.INSTANCE,
                        MethodValueResolver.INSTANCE)
                .build();
    }
}
