package edu.rutmiit.autolicence.autolicencerest.event;

import edu.rutmiit.candidateapi.candidateapicontract.dto.CandidateResponse;
import edu.rutmiit.eventing.events.CandidateEvent;
import edu.rutmiit.eventing.events.EventEnvelope;
import edu.rutmiit.eventing.events.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
public class CandidateEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(CandidateEventPublisher.class);
    private static final String SOURCE = "auto-licence-rest";

    private final RabbitTemplate rabbitTemplate;

    public CandidateEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreated(CandidateResponse candidate) {
        var event = new CandidateEvent.Created(
                candidate.getId(),
                candidate.getFullName()
        );
        send(RoutingKeys.CANDIDATE_CREATED, event);
    }

    public void publishDeleted(CandidateResponse candidate, int deletedMedBlanksCount) {
        var event = new CandidateEvent.Deleted(
                candidate.getId(),
                candidate.getFullName()
        );
        send(RoutingKeys.CANDIDATE_DELETED, event);
    }

    private void send(String routingKey, CandidateEvent event) {
        try {
            EventEnvelope<CandidateEvent> envelope = EventEnvelope.wrap(event, SOURCE, routingKey);
            rabbitTemplate.convertAndSend(RoutingKeys.EXCHANGE, routingKey, envelope);
            log.info("Событие отправлено: {} [eventId={}]", routingKey, envelope.metadata().eventId());
        } catch (Exception e) {
            log.error("Не удалось отправить событие {}: {}", routingKey, e.getMessage());
        }
    }
}
