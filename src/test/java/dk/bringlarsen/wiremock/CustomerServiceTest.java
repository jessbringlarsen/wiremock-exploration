package dk.bringlarsen.wiremock;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static dk.bringlarsen.wiremock.CustomerDTOBuilder.create;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import com.github.tomakehurst.wiremock.client.WireMock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dk.bringlarsen.wiremock.model.CustomerDTO;
import wiremock.org.apache.http.HttpStatus;

/**
 * Test that utilize WireMock as a external process to stub an external service.
 *  
 */
public class CustomerServiceTest {

    private String endpoint;
    private CustomerDTO aCustomer;
    private CustomerService service;

    @Before
    public void setup() {
        service = new CustomerService();
        CustomerServiceEndpointBuilder serviceEndpointBuilder = CustomerServiceEndpointBuilder.create();
        WireMock.configureFor(serviceEndpointBuilder.getHost(), serviceEndpointBuilder.getPort());

        aCustomer = create().withId(1).withName("Customer name").withAge(35).build();
        endpoint = serviceEndpointBuilder.withId(aCustomer.getId()).buildRelative();
    }

    @Test
    public void whenCustomerRequestExpectHttpStatusOk() {
        givenThat(get(urlEqualTo(endpoint))
            .willReturn(okForJson(aCustomer)));

        CustomerDTO response = service.getCustomerById(aCustomer.getId()).get();

        assertThat(response.getId(), is(aCustomer.getId()));
        assertThat(response.getName(), is(aCustomer.getName()));
        assertThat(response.getAge(), is(aCustomer.getAge()));
    }

    @Test
    public void whenInternalServerErrorExpectNoResponse() {
        givenThat(get(urlEqualTo(endpoint))
                .willReturn(aResponse().withStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR)));

        Optional<CustomerDTO> response = service.getCustomerById(aCustomer.getId());

        assertThat(response.isPresent(), is(false));
    }

    @After
    public void tearDown() {
        WireMock.delete(endpoint);
    }
}