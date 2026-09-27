package edu.rutmiit.demo.auditservice.listener;

import edu.rutmiit.demo.auditservice.model.AuditEntry;
import edu.rutmiit.demo.auditservice.storage.AuditStorage;
import edu.rutmiit.eventing.events.CandidateEvent;
import edu.rutmiit.eventing.events.MedBlankEvent;
import edu.rutmiit.eventing.events.EventMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;

@Component
public class AuditEventListener {

    private static final Logger log = LoggerFactory.getLogger(AuditEventListener.class);

    private final AuditStorage auditStorage;
    private final JsonMapper jsonMapper;

    public AuditEventListener(AuditStorage auditStorage, JsonMapper jsonMapper) {
        this.auditStorage = auditStorage;
        this.jsonMapper = jsonMapper;
    }


    @RabbitListener(queues = "q.audit.events", messageConverter = "")
    public void handleEvent(Message message) {
        try {
            byte[] body = message.getBody();
            JsonNode root = jsonMapper.readTree(body);

            // Извлекаем метаданные из JSON-конверта
            JsonNode metaNode = root.get("metadata");
            EventMetadata metadata = jsonMapper.treeToValue(metaNode, EventMetadata.class);

            // Дедупликация — если событие уже обработано, пропускаем
            if (auditStorage.isDuplicate(metadata.eventId())) {
                log.warn("Дубликат события пропущен: eventId={}", metadata.eventId());
                return;
            }

            // Определяем тип события и формируем описание
            JsonNode payloadNode = root.get("payload");
            String description = buildDescription(metadata.eventType(), payloadNode);

            AuditEntry entry = auditStorage.save(new AuditEntry(
                    0,
                    metadata.eventId(),
                    metadata.eventType(),
                    metadata.source(),
                    metadata.timestamp(),
                    Instant.now(),
                    description
            ));

            log.info("[AUDIT #{}] {} | {}", entry.sequenceNumber(), metadata.eventType(), description);

        } catch (Exception e) {
            log.error("Ошибка обработки события: {}", e.getMessage(), e);
            // Исключение пробросится, сообщение уйдёт в DLQ после исчерпания retries
            throw new RuntimeException("Не удалось обработать событие", e);
        }
    }


    private String buildDescription(String eventType, JsonNode payloadNode) throws Exception {
        return switch (eventType) {
            case "medBlank.created" -> {
                MedBlankEvent.Created e = jsonMapper.treeToValue(payloadNode, MedBlankEvent.Created.class);
                yield String.format("Создан медБланк «%s» (BlankCode: %s), кандидат: %s",
                        e.blankCode(), e.candidateId());
            }
            case "medBlank.updated" -> {
                MedBlankEvent.Updated e = jsonMapper.treeToValue(payloadNode, MedBlankEvent.Updated.class);
                yield String.format("Обновлён медБланк id=%d «%s»", e.candidateId(), e.blankCode());
            }
            case "medBlank.deleted" -> {
                MedBlankEvent.Deleted e = jsonMapper.treeToValue(payloadNode, MedBlankEvent.Deleted.class);
                yield String.format("Удалён медБланк «%s»",e.blankCode());
            }
            case "candidate.created" -> {
                CandidateEvent.Created e = jsonMapper.treeToValue(payloadNode, CandidateEvent.Created.class);
                yield String.format("Создан кандидат «%s»",
                        e.fullName());
            }
            case "candidate.deleted" -> {
                CandidateEvent.Deleted e = jsonMapper.treeToValue(payloadNode, CandidateEvent.Deleted.class);
                yield String.format("Удалён кандидат «%s»",
                        e.fullName());
            }
            case "medBlank.enriched" -> {
                MedBlankEvent.Enriched e = jsonMapper.treeToValue(payloadNode, MedBlankEvent.Enriched.class);
                yield String.format("Детализация медкарты",
                        e.MedBlankId(), e.blankCode(), e.eyeCheck(),
                        e.psyhicCheck(), e.nervousSystemCheck(), e.candidateHealthCheckResults());
            }
            default -> "Неизвестное событие: " + eventType;
        };
    }
}
