package edu.rutmiit.candidateapi.candidateapicontract.dto;

import edu.rutmiit.candidateapi.candidateapicontract.validation.ValidBlankCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;


@Schema(description = "Полное обновление мед данных (PUT). Все обязательные поля должны присутствовать. "
        + "ФИО не меняется. Для этого нужно создать новый бланк")
public record UpdateMedBlankRequest(

        @Schema(description = "Код бланка", example = "MOS_GOV-CLINIC7777_123456789012", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Код блана должен быть заполнен")
        @Size(max = 40, message = "Номер бланка не может быть длиннее 40ка символов")
        @ValidBlankCode
        String blankCode,

        @Schema(description = "Полис ОМС", example = "7275675876595", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Полис ОМС должен быть заполнен")
        @Size(max = 15, message = "Номер полиса не может быть длиннее 15ти символов")
        String omsId,

        @Schema(description = "Введите результат обследования зрения кандидата", example = "Зрение +1. Аномалии глаза отсутствуют.")
        @Size(max = 1000, message = "Протокол обследования не может превышать 1000 символов")
        String eyeCheck,

        @Schema(description = "Введите результат обследования психологического состояния кандидата", example = "Состояние психического здоровья пациента. Психологические заболевания отсутствуют")
        @Size(max = 1000, message = "Протокол обследования не может превышать 1000 символов")
        String psyhicCheck,

        @Schema(description = "Введите результат обследования зрения кандидата", example = "Нервная система пациента не имеет патологий. Реакция на раздражители нормальная.")
        @Size(max = 1000, message = "Протокол обследования не может превышать 1000 символов")
        String nervousSystemCheck,

        @Schema(description = "Заключение о состоянии здоровья кадидата в водители", example = "Пациент не имеет противопоказаний для получения водительских прав")
        @Size(max = 100, message = "Протокол обследования не может превышать 100 символов")
        String candidateHealthCheckResult


) {}