package edu.rutmiit.autolicence.autolicencerest.graphql.types;

//Входной тип для создания медбланка. cоответствует input CreateMedBlankInput в GraphQL-схеме

public record CreateMedBlankInputGql(
        String blankCode,
        String omsId,
        String candidateId,
        String eyeCheck,
        String psyhicCheck,
        String nervousSystemCheck,
        String candidateHealthCheckResult
) {}
