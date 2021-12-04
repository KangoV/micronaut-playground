package io.belldj.pg.mn;

import io.belldj.pg.clients.client.web.AddClientT;
import io.belldj.pg.clients.client.web.ClientT;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import jakarta.inject.Inject;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

import static io.belldj.pg.clients.client.api.ClientStatus.ACTIVE;
import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

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

    private static final Collection<String> received = new ConcurrentLinkedDeque<>();

    @Container
    private static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @Inject Flyway flyway;
    @Inject EventsListener eventsListener;
    @Inject ClientEventFT.Clients clients;

    @Inject @Client("/")
    HttpClient client;

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
        return Collections.singletonMap("kafka.bootstrap.servers", kafka.getBootstrapServers());
    }

    @Test
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
        kafka.getBootstrapServers();
        await().atMost(5, SECONDS).until(() -> !received.isEmpty());

        System.out.println(kafka.getLogs());

        assertThat(received).hasSize(1);
        var event = received.iterator().next();
        assertThat(event).isNotNull();

    }

}