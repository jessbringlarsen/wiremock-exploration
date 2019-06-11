package dk.bringlarsen.wiremock;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static dk.bringlarsen.model.builder.CustomerDTOBuilder.aCustomer;
import static org.hamcrest.Matchers.is;

import com.github.tomakehurst.wiremock.client.WireMock;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import dk.bringlarsen.wiremock.model.CustomerDTO;

/**
 * Demonstrate using wiremock for doing verification of endpoint response using the {@link WebTestClient}.
 * Using the {@link WebTestClient} we are able to decouple us from the response object by only asserting
 * the fields we are interested in. This is particular important if the api we are utilizing is not under 
 * our control and we want to avoid brittle tests as the api is changing.
 * </p>
 * Test that utilize external process instance of WireMock to stub an external
 * service which must be running for the tests to run.
 * </p>
 * For simplicity and transparency no connection strings have been refactored to a builder 
 * or static strings - which is recommended for production code.
 */
public class CustomerApiTest {

    private WebTestClient webTestClient;
    
    @Before
    public void setup() {
        WireMock.configureFor("http", "localhost", 8080);
        WireMock.resetToDefault();
        
        webTestClient = WebTestClient.bindToServer()
        .baseUrl("http://localhost:8080/")
        .build();
    }

    /**
     * Example test showing the use of WireMock and WebTestClient for verifying the response.
     */
    @Test
    public void whenCustomerRequestExpectNonEmptyResponse() {
        CustomerDTO aCustomer = aCustomer().withId(1).withName("aName").withAge(35).build();

        WireMock.givenThat(get(urlEqualTo("/customers/1"))
            .willReturn(okForJson(aCustomer)));
      
          webTestClient.get().uri("/customers/1").exchange()
            .expectStatus().isOk()
            .expectBody()
              .jsonPath("id").value(is(aCustomer.getId()))
              .jsonPath("name").value(is(aCustomer.getName()));
    }
}