package com.intive.patronage.toz.mail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.JsonNodeValueResolver;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.TemplateCache;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.intive.patronage.toz.mail.model.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.intive.patronage.toz.config.ApiUrl.ACTIVATION_PATH;
import static com.intive.patronage.toz.util.ModelMapper.convertToJsonString;

@Service
class MailTemplatesService {

    private static final String REGISTRATION_TEMPLATE_NAME = "Registration";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String baseUrl;

    private Handlebars handlebarsTemplates;

    @Autowired
    MailTemplatesService(@Value("${mail.templates.path}") String mailTemplatesPath,
                         @Value("${mail.templates.extension}") String templatesExtension,
                         @Value("${server.external.host}") String baseUrl,
                         MailTemplateCache templateCache) {
        this.baseUrl = baseUrl;
        this.handlebarsTemplates = createHandlebarsWithCache(mailTemplatesPath, templatesExtension, templateCache.getCache());
    }

    String getRegistrationTemplate(String token) throws IOException {
        final Registration registration = Registration.of(createActivationUrl(), token);
        final JsonNode jsonNode = OBJECT_MAPPER.readValue(convertToJsonString(registration), JsonNode.class);
        final Template template = getCompiledTemplate(REGISTRATION_TEMPLATE_NAME);
        return template.apply(getContext(jsonNode));
    }

    String createActivationUrl() {
        return String.format("%s%s", baseUrl, ACTIVATION_PATH);
    }

    private static Handlebars createHandlebarsWithCache(String mailTemplatesPath, String templatesExtension, TemplateCache templateCache) {
        final TemplateLoader loader = new ClassPathTemplateLoader(mailTemplatesPath, templatesExtension);
        return new Handlebars(loader).with(templateCache);
    }

    private Template getCompiledTemplate(String templateName) throws IOException {
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
