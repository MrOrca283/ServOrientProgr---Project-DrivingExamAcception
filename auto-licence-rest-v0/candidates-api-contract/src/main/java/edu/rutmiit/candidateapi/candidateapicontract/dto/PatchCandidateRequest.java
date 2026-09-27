package edu.rutmiit.candidateapi.candidateapicontract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Schema(description = "Частичное обновление кандадата (PATCH). Передать только те поля, которые нужно изменить.")
public record PatchCandidateRequest(

        @Schema(description = "Фамилия кандидата", example = "Сгуладзе")
        String lastName,

        @Schema(description = "Имя кандидата", example = "Лев")
        String firstName,

        @Schema(description = "Отчество кандидата", example = "Николаевич")
        String patronymicName,

        @Schema(description = "Полное ФИО", example = "Сгуладзе Лев Николаевич")
        String fullName,

        @Schema(description = "Номер телефона для связи", example = "+7(925)345-19-18") //LevPrav@example.ru
        String phoneNumber,

        @Schema(description = "Данные паспорта (серия, номер)", example = "4618 213288")
        String passportData,

        @Schema(description = "Наличие действующего лишения прав", example = "Нет" )
        String currentLicenceRevocation,

        @Schema(description = "Категория прав", example = "В1" )
        String licenceCategory

) {}
