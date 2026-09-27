package edu.rutmiit.autolicence.autolicencerest.graphql.types;

import java.time.LocalDate;

//Входной тип для создания кандидата. Соответствует input CreateCandidateInput в GraphQL-схеме.

public record CreateCandidateInputGql<currentLicenceRevocation>(
        String firstName,
        String lastName,
        String patronymicName,
        String phoneNumber,
        String passportData,
        String currentLicenceRevocation,
        String licenceCategory
) {}
