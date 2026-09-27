package edu.rutmiit.autolicence.autolicencerest.graphql.types;



import edu.rutmiit.candidateapi.candidateapicontract.dto.MedBlankResponse;

import java.util.List;

//Тип-обёртка для постраничного ответа, список мед бланков

public record MedBlankConnectionGql(
        List<MedBlankResponse> content,
        PageInfoGql pageInfo,
        int totalElements
) {}
