package edu.rutmiit.autolicence.autolicencerest.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import edu.rutmiit.autolicence.autolicencerest.graphql.types.CandidateConnectionGql;
import edu.rutmiit.autolicence.autolicencerest.graphql.types.CreateCandidateInputGql;
import edu.rutmiit.autolicence.autolicencerest.graphql.types.PageInfoGql;
import edu.rutmiit.autolicence.autolicencerest.graphql.types.UpdateCandidateInputGql;
import edu.rutmiit.autolicence.autolicencerest.service.CandidateService;
import edu.rutmiit.candidateapi.candidateapicontract.dto.CandidateRequest;
import edu.rutmiit.candidateapi.candidateapicontract.dto.CandidateResponse;
import edu.rutmiit.candidateapi.candidateapicontract.dto.PagedResponse;


@DgsComponent
public class CandidateDataFetcher {

    private final CandidateService candidateService;

    public CandidateDataFetcher(CandidateService candidateService) {
        this.candidateService = candidateService;
    }


    @DgsQuery(field = "candidateById")
    public CandidateResponse candidateById(@InputArgument String id) {
        return candidateService.findById(Long.parseLong(id));
    }


    @DgsQuery
    public CandidateConnectionGql candidates(
            @InputArgument Integer page,
            @InputArgument Integer size
    ) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;
        PagedResponse<CandidateResponse> paged = candidateService.findAll(pageNum, pageSize);
        return new CandidateConnectionGql(
                paged.content(),
                new PageInfoGql(paged.pageNumber(), paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements()
        );
    }


    @DgsMutation
    public CandidateResponse createCandidate(@InputArgument CreateCandidateInputGql input) {
        CandidateRequest request = new CandidateRequest(
                input.firstName(),
                input.lastName(),
                input.patronymicName(),
                input.phoneNumber(),
                input.passportData(),
                input.licenceCategory(),
                input.currentLicenceRevocation()
        );
        return candidateService.create(request);
    }

    @DgsMutation
    public CandidateResponse updateCandidate(@InputArgument String id, @InputArgument UpdateCandidateInputGql input) {
        CandidateRequest request = new CandidateRequest(
                input.firstName(),
                input.lastName(),
                input.patronymicName(),
                input.phoneNumber(),
                input.passportData(),
                input.licenceCategory(),
                input.currentLicenceRevocation()
        );
        return candidateService.update(Long.parseLong(id), request);
    }
    
    @DgsMutation
    public boolean deleteCandidate(@InputArgument String id) {
        candidateService.delete(Long.parseLong(id));
        return true;
    }
}
