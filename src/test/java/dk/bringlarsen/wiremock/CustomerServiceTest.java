package dk.bringlarsen.wiremock;

import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import dk.bringlarsen.wiremock.model.CustomerDTO;
import org.apache.http.HttpStatus;
import org.junit.Rule;
import org.junit.Test;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static dk.bringlarsen.model.builder.CustomerDTOBuilder.aCustomer;
import static dk.bringlarsen.wiremock.CustomerService.customerService;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(Options.DYNAMIC_PORT);

    @Test
    public void whenCustomerRequestExpectHttpStatusOk() {
        CustomerDTO aCustomer = aCustomer().withId(1).withName("aName").withAge(35).build();
        givenThat(get(urlEqualTo("/customers/1"))
            .willReturn(okForJson(aCustomer)));

        CustomerDTO response = customerService(wireMockRule.port()).getCustomerById(aCustomer.getId()).get();

        assertThat(response.getId(), is(aCustomer.getId()));
        assertThat(response.getName(), is(aCustomer.getName()));
        assertThat(response.getAge(), is(aCustomer.getAge()));
    }

    @Test
    public void whenInternalServerErrorExpectNoResponse() {
        givenThat(get(urlEqualTo("/customers/1"))
                .willReturn(aResponse()
                .withStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR)));

        Optional<CustomerDTO> response = customerService(wireMockRule.port()).getCustomerById(1);

        assertThat(response.isPresent(), is(false));
    }
}