package com.feng.kafka.opentracing;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Collection;
import java.util.Collections;

/**
 * @author fengsy
 * @date 3/21/21
 * @Description
 */
public class TracingCallback implements Callback {
    private final Callback callback;
    private Collection<SpanDecorator> spanDecorators;
    private final Span span;
    private final Tracer tracer;

    public TracingCallback(Callback callback, Span span, Tracer tracer) {
        this.callback = callback;
        this.span = span;
        this.tracer = tracer;
        this.spanDecorators = Collections.singletonList(SpanDecorator.STANDARD_TAGS);
    }

    TracingCallback(Callback callback, Span span, Tracer tracer, Collection<SpanDecorator> spanDecorators) {
        this.callback = callback;
        this.span = span;
        this.tracer = tracer;
        this.spanDecorators = spanDecorators;
    }

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (exception != null) {
            for (SpanDecorator decorator : spanDecorators) {
                decorator.onError(exception, span);
            }
        }

        try (Scope ignored = tracer.scopeManager().activate(span, false)) {
            if (callback != null) {
                callback.onCompletion(metadata, exception);
            }
        } finally {
            span.finish();
        }
    }
}
