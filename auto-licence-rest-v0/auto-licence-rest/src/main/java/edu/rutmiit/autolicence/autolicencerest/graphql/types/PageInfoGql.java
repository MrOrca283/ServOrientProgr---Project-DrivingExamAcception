package edu.rutmiit.autolicence.autolicencerest.graphql.types;


//Метаданные, пагинация.Тип PageInfo в GraphQL-схеме.


public record PageInfoGql(
        int pageNumber,
        int pageSize,
        int totalPages,
        boolean last
) {}
