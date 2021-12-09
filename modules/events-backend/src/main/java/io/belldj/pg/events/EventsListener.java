package io.belldj.pg.events;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.print.Book;

@Requires(notEnv = Environment.TEST) 
@KafkaListener 
public class EventsListener {

    private static final Logger log = LoggerFactory.getLogger(EventsListener.class);

    @Topic("events")
    public void notify(String string) {
         log.info("got: "+string);
    }

}