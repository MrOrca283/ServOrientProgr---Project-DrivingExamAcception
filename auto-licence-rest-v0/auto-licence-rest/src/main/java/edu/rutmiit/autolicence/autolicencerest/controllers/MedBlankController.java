package edu.rutmiit.autolicence.autolicencerest.controllers;

import edu.rutmiit.autolicence.autolicencerest.assemblers.MedBlankModelAssembler;
import edu.rutmiit.autolicence.autolicencerest.service.MedBlankService;
import edu.rutmiit.candidateapi.candidateapicontract.dto.*;
import edu.rutmiit.candidateapi.candidateapicontract.endpoints.MedBlankApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MedBlankController implements MedBlankApi {

    private final MedBlankService medBlankService;
    private final MedBlankModelAssembler medBlankModelAssembler;
    private final PagedResourcesAssembler<MedBlankResponse> pagedResourcesAssembler;

    public MedBlankController(MedBlankService medBlankService, MedBlankModelAssembler medBlankModelAssembler,
                              PagedResourcesAssembler<MedBlankResponse> pagedResourcesAssembler) {
        this.medBlankService = medBlankService;
        this.medBlankModelAssembler = medBlankModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    public EntityModel<MedBlankResponse> getMedBlankById(Long id) {
        return medBlankModelAssembler.toModel(medBlankService.findMedBlankById(id));
    }

    @Override
    public PagedModel<EntityModel<MedBlankResponse>> getAllMedBlanks(Long candidateId, String omsId,
                                                            String blankCodeSearch, int page, int size) {
        PagedResponse<MedBlankResponse> paged = medBlankService.findAllMedBlanks(candidateId, omsId, blankCodeSearch, page, size);
        Page<MedBlankResponse> springPage = new PageImpl<>(
                paged.content(),
                PageRequest.of(paged.pageNumber(), paged.pageSize()),
                paged.totalElements()
        );
        return pagedResourcesAssembler.toModel(springPage, medBlankModelAssembler);
    }

    @Override
    public ResponseEntity<EntityModel<MedBlankResponse>> createMedBlank(MedBlankRequest request) {
        MedBlankResponse created = medBlankService.createMedBlank(request);
        EntityModel<MedBlankResponse> model = medBlankModelAssembler.toModel(created);
        return ResponseEntity
                .created(model.getRequiredLink("self").toUri())
                .body(model);
    }

    @Override
    public EntityModel<MedBlankResponse> updateMedBlank(Long id, UpdateMedBlankRequest request) {
        return medBlankModelAssembler.toModel(medBlankService.updateMedBlank(id, request));
    }

    @Override
    public EntityModel<MedBlankResponse> patchMedBlank(Long id, PatchMedBlankRequest request) {
        return medBlankModelAssembler.toModel(medBlankService.patchMedBlank(id, request));
    }

    @Override
    public void deleteMedBlank(Long id) {
        medBlankService.deleteMedBlank(id);
    }
}