package edu.rutmiit.autolicence.autolicencerest.event;

import edu.rutmiit.candidateapi.candidateapicontract.dto.MedBlankResponse;

import edu.rutmiit.eventing.events.EventEnvelope;
import edu.rutmiit.eventing.events.MedBlankEvent;
import edu.rutmiit.eventing.events.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
public class MedBlankEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(MedBlankEventPublisher.class);
    private static final String SOURCE = "auto-licence-rest";

    private final RabbitTemplate rabbitTemplate;

    public MedBlankEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreated(MedBlankResponse medBlank) {
        var event = new MedBlankEvent.Created(
                medBlank.getMedBlankId(),
                medBlank.getBlankCode(),
                medBlank.getCandidate() != null ? medBlank.getCandidate().getFullName() : "Аноним?"
        );
        send(RoutingKeys.MEDBLANK_CREATED, event);
    }

    public void publishUpdated(MedBlankResponse medBlank) {
        var event = new MedBlankEvent.Updated(
                medBlank.getMedBlankId(),
                medBlank.getBlankCode(),
                medBlank.getOmsId()
        );
        send(RoutingKeys.MEDBLANK_UPDATED, event);
    }

    public void publishDeleted (String blankCode) {
        var event = new MedBlankEvent.Deleted(blankCode);
        send(RoutingKeys.MEDBLANK_DELETED, event);
    }

    private void send(String routingKey, MedBlankEvent event) {
        try {
            EventEnvelope<MedBlankEvent> envelope = EventEnvelope.wrap(event, SOURCE, routingKey);
            rabbitTemplate.convertAndSend(RoutingKeys.EXCHANGE, routingKey, envelope);
            log.info("Событие отправлено: {} [eventId={}]", routingKey, envelope.metadata().eventId());
        } catch (Exception e) {
            log.error("Не удалось отправить событие {}: {}", routingKey, e.getMessage());
        }
    }
}
