package com.feng.kafka.opentracing;

import java.util.Collection;
import java.util.Collections;
import java.util.function.BiFunction;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import io.opentracing.Tracer;

/**
 * @author fengsy
 * @date 3/22/21
 * @Description
 */
public class TracingKafkaConsumerBuilder<K, V> {
    private Collection<SpanDecorator> spanDecorators;
    private Consumer<K, V> consumer;
    private Tracer tracer;
    private BiFunction<String, ConsumerRecord, String> consumerSpanNameProvider;

    public TracingKafkaConsumerBuilder(Consumer<K, V> consumer, Tracer tracer) {
        this.tracer = tracer;
        this.consumer = consumer;
        this.spanDecorators = Collections.singletonList(SpanDecorator.STANDARD_TAGS);
        this.consumerSpanNameProvider = ClientSpanNameProvider.CONSUMER_OPERATION_NAME;
    }

    public TracingKafkaConsumerBuilder withDecorators(Collection<SpanDecorator> spanDecorators) {
        this.spanDecorators = Collections.unmodifiableCollection(spanDecorators);
        return this;
    }

    public TracingKafkaConsumerBuilder
        withSpanNameProvider(BiFunction<String, ConsumerRecord, String> consumerSpanNameProvider) {
        this.consumerSpanNameProvider = consumerSpanNameProvider;
        return this;
    }

    public TracingKafkaConsumer<K, V> build() {
        return new TracingKafkaConsumer<>(consumer, tracer, spanDecorators, consumerSpanNameProvider);
    }
}
