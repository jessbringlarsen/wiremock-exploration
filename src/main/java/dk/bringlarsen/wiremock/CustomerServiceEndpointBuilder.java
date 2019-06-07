package dk.bringlarsen.wiremock;

import org.springframework.web.util.UriComponentsBuilder;

public class CustomerServiceEndpointBuilder {

    private String path  = "/customers";
    private String host = "localhost";
    private int port = 8080;
    private int id;

    public static CustomerServiceEndpointBuilder create() {
        return new CustomerServiceEndpointBuilder();
    }

    public CustomerServiceEndpointBuilder withHost(String host) {
        this.host = host;
        return this;
    }

    public CustomerServiceEndpointBuilder withPort(int port) {
        this.port = port;
        return this;
    }

    public CustomerServiceEndpointBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public String build() {
        return UriComponentsBuilder.newInstance()
            .scheme("http")
            .host(host)
            .port(port)
            .path(path)
            .pathSegment(String.valueOf(id))
            .build()
            .toUriString();
    }

    public String buildRelative() {
        return UriComponentsBuilder.newInstance()
            .path(path)
            .pathSegment(String.valueOf(id))
            .build()
            .toUriString();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
