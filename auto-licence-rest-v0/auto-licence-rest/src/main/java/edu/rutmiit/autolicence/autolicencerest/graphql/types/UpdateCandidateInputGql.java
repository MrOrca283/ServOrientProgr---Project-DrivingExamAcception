package edu.rutmiit.autolicence.autolicencerest.graphql.types;

import java.time.LocalDate;

//Входной тип для обновления кандмдата.input UpdateCandidateInput GraphQL

public record UpdateCandidateInputGql(
        String firstName,
        String lastName,
        String patronymicName,
        String phoneNumber,
        String passportData,
        String currentLicenceRevocation,
        String licenceCategory
) {}
