package edu.rutmiit.candidateapi.candidateapicontract.dto;

import edu.rutmiit.candidateapi.candidateapicontract.validation.ValidPhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Запрос на создание или полное обновление кандидата")
public record CandidateRequest(

        @Schema(description = "Имя кандидата", example = "Лев", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Имя кандидата не может быть пустым")
        @Size(max = 30, message = "Имя не может превышать 30 символов")
        String firstName,

        @Schema(description = "Фамилия кандидата", example = "Сгуладзе", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Фамилия кандидата не может быть пустой")
        @Size(max = 30, message = "Фамилия не может превышать 30 символов")
        String lastName,

        @Schema(description = "Отчество кандидата", example = "Николаевич", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Отчество кандидата не может быть пустой")
        @Size(max = 30, message = "Отчество не может превышать 30 символов")
        String patronymicName,


        @Schema(description = "Номер телефона для связи", example = "+7 (925) 345-19-18", requiredMode = Schema.RequiredMode.REQUIRED) //LevPrav@example.ru
        @NotBlank(message = "Номер телефона не может быть пустым")
        @ValidPhoneNumber
        String phoneNumber,

        @Schema(description = "Данные паспорта (серия, номер)", example = "4618 213288")
        @NotBlank(message = "Данные паспорта не могут быть пустыми")
        String passportData,

        @Schema(description = "Наличие действующего лишения прав", example = "False" )
        String currentLicenceRevocation,

        @Schema(description = "Категория прав", example = "В1" )
        String licenceCategory

) {}