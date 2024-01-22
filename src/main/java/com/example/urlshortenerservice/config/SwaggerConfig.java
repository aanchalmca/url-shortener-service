package com.example.urlshortenerservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ConditionalOnExpression("'${swagger.enabled:false}' == 'true'")
@Configuration
@PropertySource("classpath:usu-swagger.properties")
@OpenAPIDefinition(info = @Info(title = "${usu.api.title}",
description = "${usu.api.description}"))
public class SwaggerConfig {
}
