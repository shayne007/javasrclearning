package com.fsy.javasrc.kafka.opentracing;

import static com.fsy.javasrc.kafka.opentracing.SpanDecorator.STANDARD_TAGS;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.ProducerFencedException;

import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;

/**
 * @author fengsy
 * @date 3/22/21
 * @Description
 */
public class TracingKafkaProducer<K, V> implements Producer<K, V> {

    private Producer<K, V> producer;
    private final Tracer tracer;
    private final BiFunction<String, ProducerRecord, String> producerSpanNameProvider;
    private Collection<SpanDecorator> spanDecorators;

    TracingKafkaProducer(Producer<K, V> producer, Tracer tracer, Collection<SpanDecorator> spanDecorators,
        BiFunction<String, ProducerRecord, String> producerSpanNameProvider) {
        this.producer = producer;
        this.tracer = tracer;
        this.spanDecorators = Collections.unmodifiableCollection(spanDecorators);
        this.producerSpanNameProvider = (producerSpanNameProvider == null)
            ? ClientSpanNameProvider.PRODUCER_OPERATION_NAME : producerSpanNameProvider;
    }

    public TracingKafkaProducer(Producer<K, V> producer, Tracer tracer) {
        this.producer = producer;
        this.tracer = tracer;
        this.spanDecorators = Collections.singletonList(STANDARD_TAGS);
        this.producerSpanNameProvider = ClientSpanNameProvider.PRODUCER_OPERATION_NAME;
    }

    public TracingKafkaProducer(Producer<K, V> producer, Tracer tracer,
        BiFunction<String, ProducerRecord, String> producerSpanNameProvider) {
        this.producer = producer;
        this.tracer = tracer;
        this.spanDecorators = Collections.singletonList(STANDARD_TAGS);
        this.producerSpanNameProvider = (producerSpanNameProvider == null)
            ? ClientSpanNameProvider.PRODUCER_OPERATION_NAME : producerSpanNameProvider;
    }

    @Override
    public void initTransactions() {
        producer.initTransactions();
    }

    @Override
    public void beginTransaction() throws ProducerFencedException {
        producer.beginTransaction();
    }

    @Override
    public void sendOffsetsToTransaction(Map<TopicPartition, OffsetAndMetadata> offsets, String consumerGroupId)
        throws ProducerFencedException {
        producer.sendOffsetsToTransaction(offsets, consumerGroupId);
    }

    @Override
    public void commitTransaction() throws ProducerFencedException {
        producer.commitTransaction();
    }

    @Override
    public void abortTransaction() throws ProducerFencedException {
        producer.abortTransaction();
    }

    @Override
    public Future<RecordMetadata> send(ProducerRecord<K, V> record) {
        return send(record, null, null);
    }

    public Future<RecordMetadata> send(ProducerRecord<K, V> record, SpanContext parent) {
        return send(record, null, parent);
    }

    @Override
    public Future<RecordMetadata> send(ProducerRecord<K, V> record, Callback callback) {
        return send(record, callback, null);
    }

    public Future<RecordMetadata> send(ProducerRecord<K, V> record, Callback callback, SpanContext parent) {
        /*
        // Create wrappedRecord because headers can be read only in record (if record is sent second time)
        ProducerRecord<K, V> wrappedRecord = new ProducerRecord<>(record.topic(),
        record.partition(),
        record.timestamp(),
        record.key(),
        record.value(),
        record.headers());
        */

        Span span =
            TracingKafkaUtils.buildAndInjectSpan(record, tracer, producerSpanNameProvider, parent, spanDecorators);
        tracer.activeSpan();
        Callback wrappedCallback = new TracingCallback(callback, span, tracer, spanDecorators);
        return producer.send(record, wrappedCallback);
    }

    @Override
    public void flush() {
        producer.flush();
    }

    @Override
    public List<PartitionInfo> partitionsFor(String topic) {
        return producer.partitionsFor(topic);
    }

    @Override
    public Map<MetricName, ? extends Metric> metrics() {
        return producer.metrics();
    }

    @Override
    public void close() {
        producer.close();
    }

    @Override
    public void close(long timeout, TimeUnit timeUnit) {
        producer.close(timeout, timeUnit);
    }

    @Override
    public void close(Duration duration) {

    }

}