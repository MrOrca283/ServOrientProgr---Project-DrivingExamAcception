package edu.rutmiit.autolicence.autolicencerest.graphql.types;

//Соответствует input MedBlankFilter в GraphQL-схеме. Все поля необязательны, только нужные фильтры

public record MedBlankFilterGql(
        String candidateId,
        String blankCode,
        String omsId,
        String blankCodeSearch
) {}
