package io.belldj.pg.mn;

import static io.belldj.pg.clients.client.api.ClientStatus.ACTIVE;
import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.belldj.pg.clients.client.api.PhoneType;
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

@Testcontainers
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientFT implements TestPropertyProvider {

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
      //received.add(event);
    }
  }

  @Container
  private static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

  private static final String DAVID = "David";
  private static final String SARAH = "Sarah";
  private static final String DAVE = "Dave";
  private static final String BALL = "Ball";

  private static final String CLIENT_ID_DAVID = "d967da01-9d66-4623-9762-68506151006c";
  private static final String CLIENT_ID_SARAH = "d967da01-9d66-4623-9762-68506151006d";

  @Inject Flyway flyway;
  @Inject Clients clients;

  @AfterEach
  public void afterEach() {
    flyway.clean();
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
  @DisplayName("GET existing client")
  void testGet() {
    var client = clients.get(CLIENT_ID_DAVID);
    assertThat(client).isNotNull();
    assertSoftly(softly -> {
      softly.assertThat(client.getId()).isEqualTo(CLIENT_ID_DAVID);
      softly.assertThat(client.getForename()).isEqualTo(DAVID);
      softly.assertThat(client.getCreated()).isCloseToUtcNow(within(2, HOURS));
      softly.assertThat(client.getPhones())
        .extracting("name", "number")
        .containsExactly(
          tuple(PhoneType.HOME.name(), "+1234123456"),
          tuple(PhoneType.WORK.name(), "+1234123456")
        );
    });
  }

  @Test
  @DisplayName("GET all clients")
  void testGetAll() {
    var allClients = this.clients.getAll();
    assertThat(allClients)
      .extracting("id", "forename")
      .containsExactly(
        tuple(CLIENT_ID_DAVID, DAVID),
        tuple(CLIENT_ID_SARAH, SARAH)
      );
  }

  @Test
  @DisplayName("POST new client")
  void testAdd() { // POST
    var command = AddClientT.builder()
      .forename(DAVID)
      .surname(BALL)
      .status(ACTIVE)
      .build();
    var clientT = this.clients.post(command);
    assertThat(clientT)
      .extracting("forename", "surname")
      .containsExactly(DAVID, BALL);
  }

  @Test
  @DisplayName("PUT existing client")
  void testSaveExists() { // PUT
    var oldClient = clients.get(CLIENT_ID_DAVID);
    var changedClient = oldClient.change(c -> c.preferredName(DAVE));
    var newClient = this.clients.put(changedClient, oldClient.getId());
    assertSoftly(softly -> {
      softly.assertThat(changedClient).extracting("forename", "preferredName").containsExactly(DAVID, DAVE);
      softly.assertThat(newClient.getVersion()).isGreaterThan(oldClient.getVersion());
    });
  }

  @Test
  @DisplayName("POST new client")
  void testSaveNew() { // PUT
    var client = clients.get(CLIENT_ID_DAVID);
    var command = AddClientT.builder()
      .forename("bill")
      .surname("gates")
      .status(ACTIVE)
      .phones(List.of(PhoneT.builder().name("HOME").number("+1234123456").build()))
      .build();
    var clientT = this.clients.post(command);
    assertSoftly(softly -> {
      softly.assertThat(clientT)
        .extracting("forename", "surname")
        .containsExactly("bill", "gates");
      softly.assertThat(client.getPhones())
        .extracting("name", "number")
        .containsExactly(
          tuple(PhoneType.HOME.name(), "+1234123456"),
          tuple(PhoneType.WORK.name(), "+1234123456")
        );
    });
  }

}
