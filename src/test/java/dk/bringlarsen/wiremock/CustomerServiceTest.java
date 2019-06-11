package dk.bringlarsen.wiremock;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static dk.bringlarsen.model.builder.CustomerDTOBuilder.aCustomer;
import static dk.bringlarsen.wiremock.CustomerService.customerService;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import com.github.tomakehurst.wiremock.client.WireMock;

import org.junit.Before;
import org.junit.Test;

import dk.bringlarsen.wiremock.model.CustomerDTO;
import wiremock.org.apache.http.HttpStatus;

/**
 * Demonstrate using wiremock for doing black box testing - in this case the
 * {@link CustomerService} is the subject under test.
 * </p>
 * We are
 * </p>
 * Test that utilize external process instance of WireMock to stub an external
 * service which must be running for the tests to run.
 * </p>
 * For simplicity and transparency no connection strings have been refactored to
 * a builder or static strings - which is recommended for production code.
 */
public class CustomerServiceTest {
   
    @Before
    public void setup() {
        WireMock.configureFor("http", "localhost", 8080);
        WireMock.resetToDefault();
    }

    @Test
    public void whenCustomerRequestExpectHttpStatusOk() {
        CustomerDTO aCustomer = aCustomer().withId(1).withName("aName").withAge(35).build();
        givenThat(get(urlEqualTo("/customers/1"))
            .willReturn(okForJson(aCustomer)));

        CustomerDTO response = customerService().getCustomerById(aCustomer.getId()).get();

        assertThat(response.getId(), is(aCustomer.getId()));
        assertThat(response.getName(), is(aCustomer.getName()));
        assertThat(response.getAge(), is(aCustomer.getAge()));
    }

    @Test
    public void whenInternalServerErrorExpectNoResponse() {
        givenThat(get(urlEqualTo("/customers/1"))
                .willReturn(aResponse()
                .withStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR)));

        Optional<CustomerDTO> response = customerService().getCustomerById(1);

        assertThat(response.isPresent(), is(false));
    }
}