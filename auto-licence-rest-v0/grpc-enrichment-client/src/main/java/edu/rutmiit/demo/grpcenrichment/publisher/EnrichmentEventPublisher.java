package edu.rutmiit.demo.grpcenrichment.publisher;

import edu.rutmiit.eventing.events.MedBlankEvent;
import edu.rutmiit.eventing.events.EventEnvelope;
import edu.rutmiit.eventing.events.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Публикация событий обогащения (medBlank.enriched) в RabbitMQ.
 *
 * Аналогичен MedBlankEventPublisher в demo-rest, но публикует другой тип события.
 * Паттерн fire-and-forget: если RabbitMQ недоступен, ошибка логируется,
 * но gRPC-вызов уже выполнен — результат не теряется полностью.
 */
@Component
public class EnrichmentEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EnrichmentEventPublisher.class);
    private static final String SOURCE = "grpc-enrichment-client";

    private final RabbitTemplate rabbitTemplate;

    public EnrichmentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Публикует событие medBlank.enriched с результатами gRPC-аналитики.
     */
    public void publishEnriched(MedBlankEvent.Enriched enrichedEvent) {
        try {
            EventEnvelope<MedBlankEvent> envelope = EventEnvelope.wrap(
                    enrichedEvent, SOURCE, RoutingKeys.MEDBLANK_ENRICHED);

            rabbitTemplate.convertAndSend(
                    RoutingKeys.EXCHANGE,
                    RoutingKeys.MEDBLANK_ENRICHED,
                    envelope);

            log.info("Событие отправлено: {} [medBlankId={}, eventId={}]",
                    RoutingKeys.MEDBLANK_ENRICHED,
                    enrichedEvent.blankCode(),
                    envelope.metadata().eventId());

        } catch (Exception e) {
            log.error("Не удалось отправить событие {}: {}",
                    RoutingKeys.MEDBLANK_ENRICHED, e.getMessage());
        }
    }
}
