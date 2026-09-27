package edu.rutmiit.autolicence.autolicencerest.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import edu.rutmiit.autolicence.autolicencerest.service.CandidateService;
import edu.rutmiit.candidateapi.candidateapicontract.dto.CandidateResponse;
import edu.rutmiit.candidateapi.candidateapicontract.dto.MedBlankResponse;


@DgsComponent
public class MedBlankCandidateDataFetcher {

    private final CandidateService candidateService;

    public MedBlankCandidateDataFetcher(CandidateService candidateService) {
        this.candidateService = candidateService;
    }


    @DgsData(parentType = "MedBlank", field = "candidate")
    public CandidateResponse candidate(DgsDataFetchingEnvironment dfe) {
        MedBlankResponse medBlank = dfe.getSource();
        if (medBlank.getCandidate() != null) {
            return medBlank.getCandidate();
        }
        
        return null;
    }
}
