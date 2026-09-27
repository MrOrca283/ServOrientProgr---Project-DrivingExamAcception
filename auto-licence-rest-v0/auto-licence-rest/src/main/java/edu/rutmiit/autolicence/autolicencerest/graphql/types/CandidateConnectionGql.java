package edu.rutmiit.autolicence.autolicencerest.graphql.types;

import edu.rutmiit.candidateapi.candidateapicontract.dto.CandidateResponse;

import java.util.List;

//Тип-обёртка для постраничного ответа, список кандидатов.

public record CandidateConnectionGql(
        List<CandidateResponse> content,
        PageInfoGql pageInfo,
        int totalElements
) {}
