package edu.rutmiit.demo.grpcanalytics.service;

import edu.rutmiit.demo.grpc.AnalyzeMedBlankRequest;
import edu.rutmiit.demo.grpc.MedBlankAnalysisResponse;
import edu.rutmiit.demo.grpc.MedBlankAnalyticsGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Нужно изменить ещё: enrich  grpc-contract и not-service

/**
 * Реализация gRPC-сервиса MedBlankAnalytics.
 *
 * Наследует сгенерированный базовый класс MedBlankAnalyticsImplBase —
 * аналог того, как REST-контроллер реализует интерфейс контракта:
 *
 *   REST:    AuthorController implements AuthorApi
 *   GraphQL: MedBlankDataFetcher с @DgsQuery
 *   gRPC:    MedBlankAnalyticsServiceImpl extends MedBlankAnalyticsGrpc.MedBlankAnalyticsImplBase
 *
 * Ключевые отличия от REST/GraphQL:
 * - Бинарный протокол (protobuf) вместо JSON — компактнее и быстрее
 * - Строго типизированный контракт (.proto) — несовместимость обнаруживается при компиляции
 * - HTTP/2 с мультиплексированием — несколько запросов в одном TCP-соединении
 * - Поддержка streaming (server, client, bidirectional) — здесь используем unary (простой запрос-ответ)
 */
public class MedBlankAnalyticsServiceImpl extends MedBlankAnalyticsGrpc.MedBlankAnalyticsImplBase {

    private static final Logger log = LoggerFactory.getLogger(MedBlankAnalyticsServiceImpl.class);

    /**
     * Обрабатывает запрос на анализ книги.
     *
     * Паттерн gRPC: метод получает request и StreamObserver для ответа.
     * StreamObserver — это callback-интерфейс:
     *   - onNext(response) — отправить ответ (для unary RPC вызывается один раз)
     *   - onCompleted()    — завершить RPC
     *   - onError(t)       — сообщить об ошибке
     *
     * Для unary RPC (один запрос → один ответ) всегда:
     *   responseObserver.onNext(response);
     *   responseObserver.onCompleted();
     */
    @Override
    public void analyzeMedBlank(AnalyzeMedBlankRequest request,
                            StreamObserver<MedBlankAnalysisResponse> responseObserver) {

        log.info("gRPC запрос: анализ мед бланка id={}",
                request.getBlankCode());

        // ─── Вычисление метрик

        //По мед бланку:
        //Код разшифровывается и получается Город, статус (гос/част) организации, тип клиники и номер.
        //Клиника в которой были пройдены обследования
        //Далее анализируются данные "зрение" - должно быть не более 4, то есть +4, -4 и между ними - ок.
        //цветовосприятие: "норма" и только.
        //время реакции на тесте: от "0.5" до "1.2" секунд

        String medicalStructureCity = estimateCity(request.getBlankCode());
        String medicalStructureArea = estimateAdministrativeArea(request.getBlankCode());
        String medicalStructureType = estimateType(request.getBlankCode());
        String medicalStructureNumber = estimateNumber(request.getBlankCode());
        String medicalStructureStatus = estimateStatus(request.getBlankCode());

        // ─── Формируем ответ ─────────────────────────────────────────
        MedBlankAnalysisResponse response = MedBlankAnalysisResponse.newBuilder()
                .setBlankCode(request.getMedBlankId())
                .setCityOfClinic(medicalStructureCity)
                .setClinicType(medicalStructureType)
                .setAllChecksResultSummary(medicalStructureStatus)
                .build();

        log.info("gRPC ответ: медбланк осмотра с кодом id={}, сделан в городе ={}мин, районе={}, типа учереждения={}, номер={}, номер={}",
                response.getBlankCode(),medicalStructureCity,medicalStructureArea,medicalStructureType,
                medicalStructureNumber);

        // Отправляем ответ клиенту и завершаем RPC
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * MOS_GOV-CLINIC1824_123714091467
     *
     * Местоположение медицинского учереждения
     * Город, статус (гос или частн), тип (больница, клиника и тп), номер который определяет
     * первой цифрой сторону света в городе (те например 2 это клиника в северо восточном АО) остальные 3 цифры просто номер клиники
     *
     **/

     // ─── Демонстрационная бизнес-логика ──────────────────────────────

    /**
     * MOS_GOV-CLINIC1824_123714091467
     * В реальном приложении — ML-модель или API внешнег сервиса.
     */
    private String estimateCity(String blankCode) {
        String cityCode = (blankCode.split("_"))[0];
        String cityName = switch (cityCode != null ? cityCode : "") {
            case "MOS", "МОС"       -> "Москва";
            case "SPB", "СПБ"        -> "Санкт-Петербург";
            case "EKB", "ЕКБ"       -> "Екатеринбург";
            case "MA", "МА"     -> "Московская область";
            case "SEV", "СЕВ"       -> "Севастополь";
            default                     -> "Россия";
        };
        return cityName;
    }

    /**
     *
     * MOS_GOV-CLINIC1824_123714091467
     */
    private String estimateStatus(String blankCode) {
        String clinicName = (blankCode.split("_"))[1];
        String statusName = (clinicName.split("-")[0]);
        String status = switch (statusName != null ? statusName : "") {
            case "GOV", "ГОС"       -> "Государственная";
            case "PRI", "ЧАСТ"        -> "Частная";
            default                     -> "";
        };
        return status;
    }

    /**
     * MOS_GOV-CLINIC1824_123714091467
     * Рекомендательный балл (0.0—10.0).
     * Демонстрационная формула: классика получает высокий балл.
     */
    private String estimateType(String blankCode) {
        String clinicName = (blankCode.split("_"))[1];
        String clinicFullName = (clinicName.split("-")[1]);
        String tempType =  clinicFullName.replaceAll("\\d", "");
        String type = switch (tempType != null ? tempType : "") {
            case "CLINIC", "КЛИНИКА"       -> "Поликлиника";
            case "HOSPITAL", "БОЛЬНИЦА"        -> "Больница";
            default                     -> "";
        };
        return type;
    }

    /**
     *
     * Классификация эпохи по году публикации.
     */
    private String estimateAdministrativeArea(String blankCode) {
        String clinicName = (blankCode.split("_"))[1];
        String clinicFullName = (clinicName.split("-")[1]);
        Character tempType =  (clinicFullName.replaceAll("\\s", "")).charAt(0);
        String type;
                type = switch (tempType) {
            case '1'       -> "Северный";
            case '2'        -> "Северо-восточный";
            case '3'        -> "Восточный";
            case '4'        -> "Юго-восточный";
            case '5'        -> "Южный";
            case '6'        -> "Юго-западный";
            case '7'        -> "Западный";
            case '8'        -> "Северо-западный";
            default                     -> "Центральный";
        };
        return type;
    }
    private String estimateNumber(String blankCode) {
        String clinicName = (blankCode.split("_"))[1];
        String clinicFullName = (clinicName.split("-")[1]);
        String number = (clinicFullName.replaceAll("\\s", ""));
        return number;
    }
}
