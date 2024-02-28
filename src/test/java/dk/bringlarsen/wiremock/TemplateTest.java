package dk.bringlarsen.wiremock;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

class TemplateTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig()
                    .dynamicPort()
                    .usingFilesUnderClasspath("dk/bringlarsen/wiremock"))
            .build();

    @Test
    @DisplayName("given a wiremock template and a GET request, expect response as specified in template")
    void request() {
        client().get().uri("/info.0.json")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("safe_title").isEqualTo("Alias hic nihil.");
    }

    WebTestClient client() {
        return WebTestClient.bindToServer().baseUrl(wireMock.baseUrl()).build();
    }
}
