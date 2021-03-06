package com.api.mercadeando.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuración para documentación con Swagger
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket mercadeandoApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    /**
     * Información sobre el desarrollador
     *
     * @return ApiInfo
     */
    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Mercadeando")
                .version("1.0")
                .description("API para comprar ricuras!")
                .contact(new Contact("Cristian Marin", "https://www.cristianmarint.com", "cristianmarint@gmail.com"))
                .license("MIT")
                .build();
    }
}
