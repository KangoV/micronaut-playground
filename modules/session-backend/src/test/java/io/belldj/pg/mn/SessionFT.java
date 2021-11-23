package io.belldj.pg.mn;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.belldj.pg.sessions.session.web.AddSessionT;
import io.belldj.pg.sessions.session.web.SessionT;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

@Testcontainers
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SessionFT {

  @Client("/sessions")
  interface Sessions {
    @Get          List<SessionT> getAll();
    @Get("/{id}") SessionT get(@PathVariable String id);
    @Post         SessionT       post(@Body AddSessionT addSession);
    @Put("/{id}") SessionT       put(@Body SessionT client, String id);
  }

  private static final String SESSION_ID_1 = "d967da01-9d66-4623-9762-68506151007c";
  private static final String SESSION_ID_2 = "d967da01-9d66-4623-9762-68506151007d";

  @Inject Flyway flyway;
  @Inject Sessions sessions;

  @AfterEach public void afterEach() {
    flyway.clean();
  }
  @BeforeEach public void beforeEach() {
    flyway.migrate();
  }

  @Test
  @DisplayName("GET existing session")
  void testGet() {
    var session = sessions.get(SESSION_ID_1);
    assertThat(session).isNotNull();
    assertSoftly(softly -> {
      softly.assertThat(session.getId()).isEqualTo(SESSION_ID_1);
      softly.assertThat(session.getCreated()).isCloseToUtcNow(within(2, HOURS));
    });
  }

  @Test
  @DisplayName("GET all sessions")
  void testGetAll() {
    var all = this.sessions.getAll();
    assertThat(all)
      .extracting("id")
      .containsExactly(
        SESSION_ID_1,
        SESSION_ID_2
      );
  }

}
