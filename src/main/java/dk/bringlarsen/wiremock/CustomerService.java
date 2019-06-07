package dk.bringlarsen.wiremock;

import java.util.Optional;

import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import dk.bringlarsen.wiremock.model.CustomerDTO;

public class CustomerService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Optional<CustomerDTO> getCustomerById(int id) {
        try {
            String endpoint = CustomerServiceEndpointBuilder.create().withId(id).build();
            return Optional.ofNullable(restTemplate.getForObject(endpoint, CustomerDTO.class));
        } catch (HttpServerErrorException e) {
            return Optional.empty();
        }
    }
}