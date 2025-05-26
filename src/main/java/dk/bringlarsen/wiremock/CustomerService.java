package dk.bringlarsen.wiremock;

import dk.bringlarsen.wiremock.model.CustomerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class CustomerService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${customer.service.port}")
    private String port;

    public static CustomerService create() {
        return new CustomerService();
    }

    public CustomerService withPort(int port) {
        this.port = String.valueOf(port);
        return this;
    }

    public Optional<CustomerDTO> getCustomerById(int id) {
        try {
            return Optional.of(restTemplate.getForObject("http://localhost:" + port + "/customers/"+id, CustomerDTO.class));
        } catch (HttpServerErrorException e) {
            return Optional.empty();
        }
    }
}