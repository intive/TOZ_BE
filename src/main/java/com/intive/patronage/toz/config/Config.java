package com.intive.patronage.toz.config;

import com.intive.patronage.toz.error.ControllerExceptionHandler;
import com.intive.patronage.toz.schedule.util.ScheduleParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Date;

@EnableWebMvc
@Configuration
@EnableSwagger2
@EnableJpaAuditing
class Config extends WebMvcConfigurerAdapter {

    private final ServletContext servletContext;

    @Value("${server.external.host}")
    private String serverHost;

    @Value("${server.external.url.context}")
    private String serverContext;

    @Value("${bcrypt.security.level}")
    private int securityLevel;

    @Autowired
    public Config(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        final SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(new byte[20]);
        return new BCryptPasswordEncoder(securityLevel, secureRandom);
    }

    private static ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Patronage 2017 TOZ-BE")
                .description("REST API made in Spring Boot")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public Docket api() {
        final String contextPath = servletContext.getContextPath();
        return handleReverseProxyMapping(new Docket(DocumentationType.SWAGGER_2))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .directModelSubstitute(LocalTime.class, String.class)
                .directModelSubstitute(Date.class, Long.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .securitySchemes(Collections.singletonList(apiKey()));
    }

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }

    private Docket handleReverseProxyMapping(Docket docket) {
        return docket
                .host(serverHost)
                .pathProvider(createPathProvider());
    }

    private RelativePathProvider createPathProvider() {
        return new RelativePathProvider(this.servletContext) {
            @Override
            public String getApplicationBasePath() {
                return serverContext;
            }
        };
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "jwt", "header");
    }

    @Bean @Autowired
    public ExceptionHandlerExceptionResolver createExceptionResolver(MessageSource messageSource, ScheduleParser scheduleParser) {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            @Override
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(
                    HandlerMethod handlerMethod, Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(
                        ControllerExceptionHandler.class).resolveMethod(exception);
                return new ServletInvocableHandlerMethod(
                        new ControllerExceptionHandler(messageSource, scheduleParser), method);
            }
        };
        exceptionResolver.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }
}
