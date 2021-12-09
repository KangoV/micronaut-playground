package io.belldj.pg.clients.domain;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.awt.print.Book;

@KafkaClient
public interface EventClient {

    @Topic("events")
    void notify(@KafkaKey String key, String string);

}