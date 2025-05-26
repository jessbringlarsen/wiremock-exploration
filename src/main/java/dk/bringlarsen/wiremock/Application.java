package dk.bringlarsen.wiremock;

import dk.bringlarsen.wiremock.model.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@SpringBootApplication
@RestController
public class Application {

    @Autowired
    CustomerService service;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/customers/search/{id}")
    public Optional<CustomerDTO> getCustomerById(@PathVariable int id) {
        return service.getCustomerById(id);
    }
}
