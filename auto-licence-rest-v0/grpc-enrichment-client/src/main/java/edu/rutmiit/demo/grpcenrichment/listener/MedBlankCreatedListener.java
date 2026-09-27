package edu.rutmiit.demo.grpcenrichment.listener;

import edu.rutmiit.eventing.events.MedBlankEvent;
import edu.rutmiit.demo.events.EventMetadata;
import edu.rutmiit.demo.grpc.AnalyzeMedBlankRequest;
import edu.rutmiit.demo.grpc.MedBlankAnalysisResponse;
import edu.rutmiit.demo.grpc.MedBlankAnalyticsGrpc;
import edu.rutmiit.demo.grpcenrichment.publisher.EnrichmentEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/**
 * Слушатель событий medBlank.created из RabbitMQ.
 *
 * Десериализация — ручная (как в audit-service), потому что EventEnvelope<T>
 * является generic-типом, и Jackson не может определить конкретный подтип T.
 */
@Component
public class MedBlankCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(MedBlankCreatedListener.class);

    private final MedBlankAnalyticsGrpc.MedBlankAnalyticsBlockingStub analyticsStub;
    private final EnrichmentEventPublisher enrichmentPublisher;
    private final JsonMapper jsonMapper;

    public MedBlankCreatedListener(MedBlankAnalyticsGrpc.MedBlankAnalyticsBlockingStub analyticsStub,
                               EnrichmentEventPublisher enrichmentPublisher,
                               JsonMapper jsonMapper) {
        this.analyticsStub = analyticsStub;
        this.enrichmentPublisher = enrichmentPublisher;
        this.jsonMapper = jsonMapper;
    }

    /**
     * Обрабатывает событие medBlank.created:
     * 1. Десериализует событие из JSON
     * 2. Формирует gRPC-запрос
     * 3. Вызывает gRPC-сервер (синхронно)
     * 4. Публикует результат как событие medBlank.enriched
     */
    @RabbitListener(queues = "q.enrichment.medBlank-created", messageConverter = "")
    public void handleMedBlankCreated(Message message) {
        try {
            // 1. Парсим JSON-конверт
            byte[] body = message.getBody();
            JsonNode root = jsonMapper.readTree(body);

            JsonNode metaNode = root.get("metadata");
            EventMetadata metadata = jsonMapper.treeToValue(metaNode, EventMetadata.class);

            JsonNode payloadNode = root.get("payload");
            MedBlankEvent.Created medBlankCreated = jsonMapper.treeToValue(payloadNode, MedBlankEvent.Created.class);

            log.info("Получено событие medBlank.created: medBlankId={}, «{}» [eventId={}]",
                    medBlankCreated.blankCode(), medBlankCreated.candidateId(), metadata.eventId());

            // 2. Формируем gRPC-запрос
            AnalyzeMedBlankRequest grpcRequest = AnalyzeMedBlankRequest.newBuilder()
                    .setMedBlankId(medBlankCreated.blankCode())
                    .setBlankCode(medBlankCreated.blankCode())
                    .build();


            MedBlankAnalysisResponse response = MedBlankAnalysisResponse.newBuilder()
                    .setBlankCode(grpcRequest.getMedBlankId())
                    .build();


            // 3. Вызываем gRPC-сервер (синхронно)
            log.info("Вызов gRPC: MedBlankAnalytics.AnalyzeMedBlank(medBlankId={})", medBlankCreated.blankCode());
            MedBlankAnalysisResponse grpcResponse = analyticsStub.analyzeMedBlank(grpcRequest);

            log.info("gRPC ответ получен: medBlankId={}, время={}мин, сложность={}, балл={}, эпоха={}",
                    grpcResponse.getBlankCode());

            // 4. Публикуем событие medBlank.enriched



        } catch (io.grpc.StatusRuntimeException e) {
            log.error("gRPC ошибка при обогащении книги: {} ({})",
                    e.getStatus().getDescription(), e.getStatus().getCode());
            throw new RuntimeException("gRPC-вызов завершился ошибкой", e);

        } catch (Exception e) {
            log.error("Ошибка обработки события medBlank.created: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось обработать событие medBlank.created", e);
        }
    }
}
