package edu.rutmiit.candidateapi.candidateapicontract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;


@Getter
@Builder
    @EqualsAndHashCode(callSuper = false) // не включаем HATEOAS-ссылки в сравнение equals
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "candidate", itemRelation = "candidate")
@Schema(description = "Информация о кандидате")
public class CandidateResponse extends RepresentationModel<CandidateResponse> {

    @Schema(description = "Уникальный идентификатор кандидата", example = "1")
    private final Long id;

    @Schema(description = "Фамилия кандидата", example = "Сгуладзе")
    private final String lastName;

    @Schema(description = "Имя кандидата", example = "Лев")
    private final String firstName;

    @Schema(description = "Отчество кандидата", example = "Николаевич")
    private final String patronymicName;

    @Schema(description = "Полное ФИО", example = "Сгуладзе Лев Николаевич")
    private final String fullName;

    @Schema(description = "Номер телефона для связи", example = "+7(925)345-19-18") //LevPrav@example.ru
    private final String phoneNumber;

    @Schema(description = "Данные паспорта (серия, номер)", example = "4618 213288")
    private final String passportData;

    @Schema(description = "Наличие действующего лишения прав", example = "Нет" )
    private final String currentLicenceRevocation;

    @Schema(description = "Категория прав", example = "В1" )
    private final String licenceCategory;
}















//Пояснения для понимания
/*

PUT
Отправляешь все поля ресурса — даже те, которые не меняешь.
Если какое-то поле не указано — сервер считает: "Это поле
нужно удалить или установить в пустое/нулевое значение".

PUT /api/v1/authors/123
{
  "name": "Иван Иванов",
  "phoneNumber": "+7 (945) 367-15-28",
  "phone": "",
  "birthDate": null,
  "bio": "Новая биография"
}




PATCH
Отправляешь только те поля, которые хочешь обновить.
Остальные поля не трогаются.


PATCH /api/v1/authors/123
{
  "bio": "Новая биография"
}








 */
