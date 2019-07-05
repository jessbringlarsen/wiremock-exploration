package dk.bringlarsen.wiremock;

import dk.bringlarsen.wiremock.model.CustomerDTO;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public class CustomerService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final int port;

    public CustomerService(int port) {
        this.port = port;
    }

    public static CustomerService customerService(int port) {
        return new CustomerService(port);
    }

    public Optional<CustomerDTO> getCustomerById(int id) {
        try {
            return Optional.ofNullable(restTemplate.getForObject("http://localhost:" + port + "/customers/"+id, CustomerDTO.class));
        } catch (HttpServerErrorException e) {
            return Optional.empty();
        }
    }
}