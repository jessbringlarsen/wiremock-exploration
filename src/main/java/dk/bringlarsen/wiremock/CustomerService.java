package dk.bringlarsen.wiremock;

import java.util.Optional;

import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import dk.bringlarsen.wiremock.model.CustomerDTO;

public class CustomerService {

    private final RestTemplate restTemplate = new RestTemplate();

    public static CustomerService customerService() {
        return new CustomerService();
    }

    public Optional<CustomerDTO> getCustomerById(int id) {
        try {
            return Optional.ofNullable(restTemplate.getForObject("http://localhost:8080/customers/"+id, CustomerDTO.class));
        } catch (HttpServerErrorException e) {
            return Optional.empty();
        }
    }
}