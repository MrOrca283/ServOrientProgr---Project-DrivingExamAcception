package edu.rutmiit.candidateapi.candidateapicontract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;


@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "MedBlank", itemRelation = "medBlank")
@Schema(description = "Медицинские сведения о кандидате")
public class MedBlankResponse extends RepresentationModel<MedBlankResponse> {

    @Schema(description = "Уникальный идентификатор медбланка", example = "1")
    private final Long medBlankId;

    @Schema(description = "BC медбланка и учереждения", example = "MOS_GOV-CLINIC1824_123714091467")
    private final String blankCode;

/*    @Schema(description = "Имя", example = "Лев")
    private final CandidateResponse firstName;

    @Schema(description = "Фамилия", example = "Сгуладзе")
    private final CandidateResponse lastName;

    @Schema(description = "Отчество", example = "Николаевич")
    private final CandidateResponse patronymicName;*/

    @Schema(description = "Кандидат")
    private final CandidateResponse candidate;

    @Schema(description = "Полис ОМС", example = "7275675876595")
    private final String omsId;

    @Schema(description = "Результат осмотра офтальмологом", example = "Годен. Зрение +1. Аномалии глаза отсутствуют.")
    private final String eyeCheck;

    @Schema(description = "Результат осмотра психотерапевтом", example = " Допуск. Состояние психического здоровья пациента. Психологические заболевания отсутствуют")
    private final String psyhicCheck;

    @Schema(description = "Результат осмотра неврологом", example = " Годен. Нервная система пациента не имеет патологий. Реакция на раздражители нормальная.")
    private final String nervousSystemCheck;

    @Schema(description = "Заключение о состоянии здоровья кадидата в водители" , example = "Пациент не имеет противопоказаний для получения водительских прав")
    private final String candidateHealthCheckResult;

}

