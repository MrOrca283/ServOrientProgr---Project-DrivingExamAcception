package edu.rutmiit.autolicence.autolicencerest.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import edu.rutmiit.autolicence.autolicencerest.graphql.types.MedBlankConnectionGql;
import edu.rutmiit.autolicence.autolicencerest.graphql.types.PageInfoGql;
import edu.rutmiit.autolicence.autolicencerest.service.MedBlankService;
import edu.rutmiit.candidateapi.candidateapicontract.dto.MedBlankResponse;
import edu.rutmiit.candidateapi.candidateapicontract.dto.PagedResponse;


@DgsComponent
public class CandidateMedBlankDataFetcher {

    private final MedBlankService medBlankService;

    public CandidateMedBlankDataFetcher(MedBlankService medBlankService) {
        this.medBlankService = medBlankService;
    }

    @DgsQuery
    public MedBlankConnectionGql medBlanks(
            @InputArgument String candidateId,
            @InputArgument Integer page,
            @InputArgument Integer size) {

        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        Long id = candidateId != null ? Long.parseLong(candidateId) : null;

        PagedResponse<MedBlankResponse> paged = medBlankService.findAllMedBlanks(
                id, null, null, pageNum, pageSize);

        return new MedBlankConnectionGql(
                paged.content(),
                new PageInfoGql(paged.pageNumber(), paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements());
    }
}
