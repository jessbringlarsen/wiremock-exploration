package dk.bringlarsen.wiremock;

import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import dk.bringlarsen.wiremock.model.CustomerDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static dk.bringlarsen.model.builder.CustomerDTOBuilder.aCustomer;
import static dk.bringlarsen.wiremock.CustomerService.customerService;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
@WireMockTest
public class CustomerServiceTest {

    @Test
    public void whenCustomerRequestExpectHttpStatusOk(WireMockRuntimeInfo wireMock) {
        CustomerDTO aCustomer = aCustomer().withId(1).withName("aName").withAge(35).build();
        givenThat(get(urlEqualTo("/customers/1"))
            .willReturn(okForJson(aCustomer)));

        CustomerDTO response = customerService(wireMock.getHttpPort()).getCustomerById(aCustomer.getId()).get();

        Assertions.assertEquals(aCustomer.getId(), response.getId());
        Assertions.assertEquals(aCustomer.getName(), response.getName());
        Assertions.assertEquals(aCustomer.getAge(), response.getAge());
    }

    @Test
    public void whenInternalServerErrorExpectNoResponse(WireMockRuntimeInfo wireMock) {
        givenThat(get(urlEqualTo("/customers/1"))
                .willReturn(aResponse()
                .withStatus(500)));

        Optional<CustomerDTO> response = customerService(wireMock.getHttpPort()).getCustomerById(1);

        assertFalse(response.isPresent());
    }
}