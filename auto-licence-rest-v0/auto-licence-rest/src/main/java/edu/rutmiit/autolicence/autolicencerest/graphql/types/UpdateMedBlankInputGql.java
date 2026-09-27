package edu.rutmiit.autolicence.autolicencerest.graphql.types;

//Входной тип для обновления медбланка, input UpdateMedBlankInput в GraphQL. Кандидат конст.

public record UpdateMedBlankInputGql(
        String blankCode,
        String omsId,
        String eyeCheck,
        String psyhicCheck,
        String nervousSystemCheck,
        String candidateHealthCheckResult
) {}
