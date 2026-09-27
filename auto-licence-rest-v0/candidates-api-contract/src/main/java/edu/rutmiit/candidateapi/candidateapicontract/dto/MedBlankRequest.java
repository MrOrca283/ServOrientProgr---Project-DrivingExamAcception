package edu.rutmiit.candidateapi.candidateapicontract.dto;

import edu.rutmiit.candidateapi.candidateapicontract.validation.ValidBlankCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;


@Schema(description = "Создание нового и полное изменение данных медицинского бланка")
public record MedBlankRequest(

        @Schema(description = "Код бланка", example = "MOS_GOV-CLINIC7777_123456789012", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Код блана должен быть заполнен")
        @Size(max = 40, message = "Номер бланка не может быть длиннее 40ка символов")
        @ValidBlankCode
        String blankCode,

/*        @Schema(description = "Имя", example = "Лев")
        @NotBlank(message = "Имя не может быть пустым")
        CandidateResponse firstName,

        @Schema(description = "Фамилия", example = "Сгуладзе")
        @NotBlank(message = "Фамилия не может быть пустой")
        CandidateResponse lastName,

        @Schema(description = "Отчество", example = "Николаевич")
        @NotBlank(message = "Отчество не может быть пустым")
        CandidateResponse patronymicName,*/

        @Schema(description = "ID кандидата",example="1",requiredMode = Schema.RequiredMode.REQUIRED)
        String candidateId,

        @Schema(description = "Полис ОМС", example = "7275675876595")
        @NotBlank(message = "Полис ОМС не может быть пустым")
        String omsId,

        @Schema(description = "Результат осмотра офтальмологом", example = "Зрение +1. Аномалии глаза отсутствуют.")
        String eyeCheck,

        @Schema(description = "Результат осмотра психотерапевтом", example = "Состояние психического здоровья пациента. Психологические заболевания отсутствуют")
        String psyhicCheck,

        @Schema(description = "Результат осмотра неврологом", example = "Нервная система пациента не имеет патологий. Реакция на раздражители нормальная.")
        String nervousSystemCheck,

        @Schema(description = "Заключение о состоянии здоровья кадидата в водители" , example = "Пациент не имеет противопоказаний для получения водительских прав")
        String candidateHealthCheckResult

) {}