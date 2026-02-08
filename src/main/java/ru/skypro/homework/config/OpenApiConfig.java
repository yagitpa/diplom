package ru.skypro.homework.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI adsPlatformOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Ads Platform API")
                                .description("API для платформы объявлений")
                                .version("1.0.0")
                                .contact(
                                        new Contact()
                                                .name("Пальчиков Яков")
                                                .email("yakov.palchikov@gmail.com"))
                                .license(
                                        new License()
                                                .name("Apache 2.0")
                                                .url(
                                                        "http://www.apache.org/licenses/LICENSE-2.0")));
    }
}
