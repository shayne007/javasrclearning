package com.feng.kafka1.dd.opentracing;

import java.util.function.BiFunction;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @author fengsy
 * @date 3/22/21
 * @Description
 */
public class ClientSpanNameProvider {

    // Operation Name as Span Name
    public static BiFunction<String, ConsumerRecord, String> CONSUMER_OPERATION_NAME =
        (operationName, consumerRecord) -> replaceIfNull(operationName, "unknown");
    public static BiFunction<String, ProducerRecord, String> PRODUCER_OPERATION_NAME =
        (operationName, producerRecord) -> replaceIfNull(operationName, "unknown");

    public static BiFunction<String, ConsumerRecord, String> CONSUMER_PREFIXED_OPERATION_NAME(final String prefix) {
        return (operationName, consumerRecord) -> replaceIfNull(prefix, "") + replaceIfNull(operationName, "unknown");
    }

    public static BiFunction<String, ProducerRecord, String> PRODUCER_PREFIXED_OPERATION_NAME(final String prefix) {
        return (operationName, producerRecord) -> replaceIfNull(prefix, "") + replaceIfNull(operationName, "unknown");
    }

    // Topic as Span Name
    public static BiFunction<String, ConsumerRecord, String> CONSUMER_TOPIC =
        (operationName, consumerRecord) -> replaceIfNull(consumerRecord, "unknown");
    public static BiFunction<String, ProducerRecord, String> PRODUCER_TOPIC =
        (operationName, producerRecord) -> replaceIfNull(producerRecord, "unknown");

    public static BiFunction<String, ConsumerRecord, String> CONSUMER_PREFIXED_TOPIC(final String prefix) {
        return (operationName, consumerRecord) -> replaceIfNull(prefix, "") + replaceIfNull(consumerRecord, "unknown");
    }

    public static BiFunction<String, ProducerRecord, String> PRODUCER_PREFIXED_TOPIC(final String prefix) {
        return (operationName, producerRecord) -> replaceIfNull(prefix, "") + replaceIfNull(producerRecord, "unknown");
    }

    // Operation Name and Topic as Span Name
    public static BiFunction<String, ConsumerRecord, String> CONSUMER_OPERATION_NAME_TOPIC = (operationName,
        consumerRecord) -> replaceIfNull(operationName, "unknown") + " - " + replaceIfNull(consumerRecord, "unknown");
    public static BiFunction<String, ProducerRecord, String> PRODUCER_OPERATION_NAME_TOPIC = (operationName,
        producerRecord) -> replaceIfNull(operationName, "unknown") + " - " + replaceIfNull(producerRecord, "unknown");

    public static BiFunction<String, ConsumerRecord, String>
        CONSUMER_PREFIXED_OPERATION_NAME_TOPIC(final String prefix) {
        return (operationName, consumerRecord) -> replaceIfNull(prefix, "") + replaceIfNull(operationName, "unknown")
            + " - " + replaceIfNull(consumerRecord, "unknown");
    }

    public static BiFunction<String, ProducerRecord, String>
        PRODUCER_PREFIXED_OPERATION_NAME_TOPIC(final String prefix) {
        return (operationName, producerRecord) -> replaceIfNull(prefix, "") + replaceIfNull(operationName, "unknown")
            + " - " + replaceIfNull(producerRecord, "unknown");
    }

    private static String replaceIfNull(String input, String replacement) {
        return (input == null) ? replacement : input;
    }

    private static String replaceIfNull(ConsumerRecord input, String replacement) {
        return ((input == null) ? replacement : input.topic());
    }

    private static String replaceIfNull(ProducerRecord input, String replacement) {
        return ((input == null) ? replacement : input.topic());
    }
}
