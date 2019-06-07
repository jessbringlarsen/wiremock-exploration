# WireMock exploration

The purpose of this project is to demonstrate the use [WireMock](http://wiremock.org) to stub external services in a component test.

One advantage of using WireMock is that we can setup the response directly in the test making it easier to understand what the test is trying to do as opposed to having a stub service that serves static responses.

The preferred way of using WireMock is to use the [WireMockRule](http://wiremock.org/docs/junit-rule/) enabling us to run WireMock in process as the test executes. If that is not possible we must start an WireMock as an [external process](http://wiremock.org/docs/running-standalone/).

    java -jar wiremock-standalone.jar

By default the server listen on `localhost:8080` but if we need to change this we can do:

     WireMock.configureFor("localhost", "8081");

After that we can setup responses in the arrange part of our test:

    import static com.github.tomakehurst.wiremock.client.WireMock.*;
    ...
    givenThat(get(urlEqualTo("/customers/1"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("{\"id\":1}")));
    ...
