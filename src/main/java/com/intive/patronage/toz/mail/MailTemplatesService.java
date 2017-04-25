package com.intive.patronage.toz.mail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.intive.patronage.toz.mail.model.Registration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.intive.patronage.toz.config.ApiUrl.ACTIVATION_PATH;
import static com.intive.patronage.toz.util.ModelMapper.convertToJsonString;

@Service
public class MailTemplatesService {

    private static final String REGISTRATION_TEMPLATE_NAME = "Registration";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static Cache<TemplateSource, Template> templateCache;
    private String mailTemplatesPath;
    private String templatesExtension;

    public MailTemplatesService(@Value("${cache.durationInMinutes}") Integer cacheDurationInMinutes,
                                @Value("${cache.maxSize}") Integer cacheMaxSize,
                                @Value("${mail.templates.path}") String mailTemplatesPath,
                                @Value("${mail.templates.extension}") String templatesExtension) {
        this.mailTemplatesPath = mailTemplatesPath;
        this.templatesExtension = templatesExtension;
        templateCache = CacheBuilder.newBuilder().expireAfterWrite(cacheDurationInMinutes, TimeUnit.MINUTES).maximumSize(cacheMaxSize).build();
    }

    public String getRegistrationTemplate(String baseUrl, String token) throws IOException {
        Registration model = new Registration();
        model.setToken(token);
        model.setUrl(String.format("%s%s", baseUrl, ACTIVATION_PATH));
        JsonNode jsonNode = OBJECT_MAPPER.readValue(convertToJsonString(model), JsonNode.class);
        Template template = getTemplate(REGISTRATION_TEMPLATE_NAME);
        return template.apply(getContext(jsonNode));
    }

    private Handlebars getHandlebarsTemplates() {
        TemplateLoader loader = new ClassPathTemplateLoader(mailTemplatesPath, templatesExtension);
        return new Handlebars(loader).with((new GuavaTemplateCache(templateCache)));
    }

    private Template getTemplate(String templateName) throws IOException {
        Handlebars handlebarsTemplates = getHandlebarsTemplates();
        return handlebarsTemplates.compile(templateName);
    }

    private Context getContext(JsonNode jsonNode) {
        return Context
                .newBuilder(jsonNode)
                .resolver(JsonNodeValueResolver.INSTANCE, JavaBeanValueResolver.INSTANCE, FieldValueResolver.INSTANCE,
                        MapValueResolver.INSTANCE,
                        MethodValueResolver.INSTANCE)
                .build();
    }
}
