package edu.rutmiit.candidateapi.candidateapicontract.dto;

import edu.rutmiit.candidateapi.candidateapicontract.validation.ValidBlankCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Schema(description = "Частичное обновление медбланка (PATCH). Передать только те поля, которые нужно изменить. "
        + "Непереданные поля остаются без изменений.")
public record PatchMedBlankRequest(

        @Schema(description = "Код бланка", example = "MOS_GOV-CLINIC7777_123456789012", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Код блана должен быть заполнен")
        @Size(max = 40, message = "Номер бланка не может быть длиннее 40ка символов")
        @ValidBlankCode
        String blankCode,

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
