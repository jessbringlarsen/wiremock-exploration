package dk.bringlarsen.wiremock;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import dk.bringlarsen.wiremock.model.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static dk.bringlarsen.model.builder.CustomerDTOBuilder.aCustomer;
import static org.hamcrest.Matchers.is;

/**
 * Demonstrate using wiremock for doing verification of endpoint response using the {@link WebTestClient}.
 * Using the {@link WebTestClient} we are able to decouple us from the response object by only asserting
 * the fields we are interested in. This is particular important if the api we are utilizing is not under 
 * our control, and we want to avoid brittle tests as the api is changing.
 * </p>
 * Test that utilize external process instance of WireMock to stub an external
 * service which must be running for the tests to run.
 * </p>
 * For simplicity and transparency no connection strings have been refactored to a builder 
 * or static strings - which is recommended for production code.
 */
@WireMockTest
public class CustomerApiTest {

    /**
     * Example test showing the use of WireMock and WebTestClient for verifying the response.
     */
    @Test
    public void whenCustomerRequestExpectNonEmptyResponse(WireMockRuntimeInfo wireMock) {
        CustomerDTO aCustomer = aCustomer().withId(1).withName("aName").withAge(35).build();

        WireMock.givenThat(get(urlEqualTo("/customers/1"))
            .willReturn(okForJson(aCustomer)));

          client(wireMock).get().uri("/customers/1").exchange()
            .expectStatus().isOk()
            .expectBody()
              .jsonPath("id").value(is(aCustomer.getId()))
              .jsonPath("name").value(is(aCustomer.getName()));
    }

    WebTestClient client(WireMockRuntimeInfo wireMock) {
        return WebTestClient.bindToServer().baseUrl(wireMock.getHttpBaseUrl()).build();
    }
}