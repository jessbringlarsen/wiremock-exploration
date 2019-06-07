package dk.bringlarsen.wiremock;

import dk.bringlarsen.wiremock.model.CustomerDTO;

public class CustomerDTOBuilder {

    private final CustomerDTO customer;

    public static CustomerDTOBuilder create() {
        return new CustomerDTOBuilder();
    }

    private CustomerDTOBuilder() {
        this.customer = new CustomerDTO();
    }
    
    public CustomerDTOBuilder withId(int id) {
        customer.setId(id);
        return this;
    }

    public CustomerDTOBuilder withName(String name) {
        customer.setName(name);
        return this;
    }

    public CustomerDTOBuilder withAge(int age) {
        customer.setAge(age);
        return this;
    }

    public CustomerDTO build() {
        return customer;
    }
}