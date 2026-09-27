package edu.rutmiit.autolicence.autolicencerest.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import edu.rutmiit.autolicence.autolicencerest.graphql.types.*;
import edu.rutmiit.candidateapi.candidateapicontract.dto.MedBlankRequest;
import edu.rutmiit.candidateapi.candidateapicontract.dto.MedBlankResponse;
import edu.rutmiit.candidateapi.candidateapicontract.dto.PagedResponse;
import edu.rutmiit.autolicence.autolicencerest.service.MedBlankService;
import edu.rutmiit.candidateapi.candidateapicontract.dto.UpdateMedBlankRequest;

@DgsComponent
public class MedBlankDataFetcher {

    private final MedBlankService medBlankService;

    public MedBlankDataFetcher(MedBlankService medBlankService) {
        this.medBlankService = medBlankService;
    }



    @DgsQuery(field = "medBlankById")
    public MedBlankResponse medBlankById(@InputArgument String id) {
        return medBlankService.findMedBlankById(Long.parseLong(id));
    }


    /*@DgsQuery
    public MedBlankConnectionGql medBlanks(
            @InputArgument MedBlankFilterGql filter,
            @InputArgument Integer page,
            @InputArgument Integer size) {

        // Подставляем значения по умолчанию, если клиент не передал аргументы
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        // Извлекаем параметры фильтрации
        Long candidateId = null;
        String omsId = null;
        String blankCodeSearch = null;

        if (filter != null) {
            candidateId = filter.candidateId() != null ? Long.parseLong(filter.candidateId()) : null;
            omsId = filter.omsId();
            blankCodeSearch = filter.blankCodeSearch();
        }

        // Переиспользуем существующий сервисный слой — GraphQL не дублирует бизнес-логику
        PagedResponse<MedBlankResponse> paged = medBlankService.findAllMedBlanks(
                candidateId, omsId, blankCodeSearch, pageNum, pageSize);

        return new MedBlankConnectionGql(
                paged.content(),
                new PageInfoGql(paged.pageNumber(), paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements());
    }*/


    @DgsMutation
    public MedBlankResponse createMedBlank(@InputArgument CreateMedBlankInputGql input) {
        MedBlankRequest request = new MedBlankRequest(
                input.blankCode(),
                input.omsId(),
                input.candidateId(),
                input.eyeCheck(),
                input.psyhicCheck(),
                input.nervousSystemCheck(),
                input.candidateHealthCheckResult()

        );
        return medBlankService.createMedBlank(request);
    }


    @DgsMutation
    public MedBlankResponse updateMedBlank(@InputArgument String id, @InputArgument UpdateMedBlankInputGql input) {
        UpdateMedBlankRequest request = new UpdateMedBlankRequest(
                input.blankCode(),
                input.omsId(),
                input.eyeCheck(),
                input.psyhicCheck(),
                input.nervousSystemCheck(),
                input.candidateHealthCheckResult()
        );
        return medBlankService.updateMedBlank(Long.parseLong(id), request);
    }


    @DgsMutation
    public boolean deleteMedBlank(@InputArgument String id) {
        medBlankService.deleteMedBlank(Long.parseLong(id));
        return true;
    }
}
