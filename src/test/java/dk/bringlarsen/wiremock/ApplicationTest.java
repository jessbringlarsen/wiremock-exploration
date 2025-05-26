package dk.bringlarsen.wiremock;

import dk.bringlarsen.wiremock.model.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.wiremock.spring.EnableWireMock;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static dk.bringlarsen.model.builder.CustomerDTOBuilder.aCustomer;

@EnableWireMock
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "customer.service.port=${wiremock.server.port}")
class ApplicationTest {

    @Test
    void testMockedService(@Autowired WebTestClient client) {
        CustomerDTO aCustomer = aCustomer().withId(1).withName("aName").withAge(35).build();
        givenThat(get(urlEqualTo("/customers/1"))
                .willReturn(okForJson(aCustomer)));

        client.get().uri("/customers/search/1").exchange().expectBody()
                .jsonPath("id").isEqualTo("1")
                .jsonPath("name").isEqualTo("aName");

    }
}
