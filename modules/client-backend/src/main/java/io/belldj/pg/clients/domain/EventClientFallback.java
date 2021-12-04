package io.belldj.pg.clients.domain;

import io.belldj.pg.clients.domain.EventClient;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;

/**
 * If you want to disable micronaut-kafka entirely, you can set kafka.enabled to false in application.yml.
 * This will prevent the instantiation of all kafka-related beans.
 * You must, however, provide your own replacement implementations of any @KafkaClient interfaces:
 */

// Only instantiate when kafka.enabled is set to false
@Requires(property = "kafka.enabled", notEquals = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
// Replace the @KafkaClient interface
@Replaces(EventClient.class)
public class EventClientFallback implements EventClient {

    @Override
    public void notify(String key, String string) {
        throw new UnsupportedOperationException();
    }

}