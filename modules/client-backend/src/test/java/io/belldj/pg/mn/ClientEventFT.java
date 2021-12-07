package io.belldj.pg.mn;

import static io.belldj.pg.clients.client.api.ClientStatus.ACTIVE;
import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;

import io.belldj.pg.clients.client.web.*;
import io.micronaut.configuration.kafka.annotation.*;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.*;
import org.testcontainers.utility.DockerImageName;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

@Testcontainers 
@MicronautTest
@TestInstance(PER_CLASS)
class ClientEventFT implements TestPropertyProvider {

    @Client("/clients")
    interface Clients {
        @Get          List<ClientT> getAll();
        @Post         ClientT       post(@Body AddClientT addClient);
        @Get("/{id}") ClientT       get(@PathVariable String id);
        @Put("/{id}") ClientT       put(@Body ClientT client, String id);
    }

    @KafkaListener(offsetReset = EARLIEST)
    static class EventsListener {
        @Topic("events")
        void receive(@KafkaKey String key,  String event) {
            System.out.println("### Received from kafka: "+event);
            received.add(event);
        }
    }

    @Container
    private static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    private static final Collection<String> received = new ConcurrentLinkedDeque<>();

    @Inject Flyway flyway;
    @Inject ClientEventFT.Clients clients;

    @AfterEach
    public void afterEach() {
        flyway.clean();
        received.clear();
    }

    @BeforeEach
    public void beforeEach() {
        flyway.migrate();
    }

    @NonNull
    @Override
    public Map<String, String> getProperties() {
        return Map.of(
          "kafka.bootstrap.servers", kafka.getBootstrapServers()
        );
    }

    @Test
    @Disabled
    @DisplayName("POST new client")
    void testAdd() { // POST

        final var DAVID = "David";
        final var BALL = "Ball";

        var command = AddClientT.builder()
          .forename(DAVID)
          .surname(BALL)
          .status(ACTIVE)
          .build();

        this.clients.post(command);
        await().atMost(20, SECONDS).until(() -> !received.isEmpty());

        System.out.println(kafka.getLogs());

        assertThat(received).hasSize(1);
        var event = received.iterator().next();
        assertThat(event).isNotNull();

    }

}