package ru.skypro.homework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.PostgreSQLContainer;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

    protected static final PostgreSQLContainer<?> postgres;

    static {
        // Принудительно устанавливаем версию Docker API
        System.setProperty("docker.client.version", "1.44");
        // Отключаем Ryuk (рекомендуется для CI)
        System.setProperty("testcontainers.ryuk.disabled", "true");
        postgres = new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test")
                .withStartupTimeout(Duration.ofMinutes(1));
        postgres.start();
        Runtime.getRuntime().addShutdownHook(new Thread(postgres::stop));
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("app.image.avatar-dir", () -> "./target/test-avatars");
        registry.add("app.image.ad-dir", () -> "./target/test-ads-images");
        registry.add("spring.datasource.hikari.connection-timeout", () -> "60000");
        registry.add("spring.datasource.hikari.validation-timeout", () -> "60000");
    }

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected String baseUrl() {
        return "http://localhost:" + port;
    }

    protected TestRestTemplate withAuth(String username, String password) {
        return restTemplate.withBasicAuth(username, password);
    }

    // Для PATCH-запросов с телом (не multipart)
    protected <T> ResponseEntity<T> patchWithAuth(String url, Object request, Class<T> responseType,
                                                  String username, String password, Object... uriVariables) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        HttpEntity<?> entity = new HttpEntity<>(request, headers);
        return restTemplate.exchange(url, HttpMethod.PATCH, entity, responseType, uriVariables);
    }

    // Для multipart PATCH-запросов (например, обновление изображения)
    protected <T> ResponseEntity<T> patchMultipartWithAuth(String url, MultiValueMap<String, Object> body,
                                                           Class<T> responseType, String username, String password,
                                                           Object... uriVariables) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.PATCH, entity, responseType, uriVariables);
    }

    // Создание директорий для изображений (вызывать в setUp() наследников)
    protected void createImageDirectories() {
        try {
            Files.createDirectories(Paths.get("./target/test-avatars"));
            Files.createDirectories(Paths.get("./target/test-ads-images"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test image directories", e);
        }
    }
}