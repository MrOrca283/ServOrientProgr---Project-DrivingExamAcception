package edu.rutmiit.autolicence.autolicencerest.controllers;


import edu.rutmiit.autolicence.autolicencerest.assemblers.CandidateModelAssembler;
import edu.rutmiit.autolicence.autolicencerest.assemblers.MedBlankModelAssembler;
import edu.rutmiit.autolicence.autolicencerest.service.CandidateService;
import edu.rutmiit.autolicence.autolicencerest.service.MedBlankService;
import edu.rutmiit.candidateapi.candidateapicontract.dto.*;
import edu.rutmiit.candidateapi.candidateapicontract.endpoints.CandidateApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CandidateController implements CandidateApi {

    private final CandidateService candidateService;
    private final MedBlankService medBlankService;
    private final CandidateModelAssembler candidateModelAssembler;
    private final MedBlankModelAssembler medBlankModelAssembler;
    private final PagedResourcesAssembler<CandidateResponse> pagedCandidatesAssembler;
    private final PagedResourcesAssembler<MedBlankResponse> pagedMedBlanksAssembler;

    public CandidateController(CandidateService candidateService,
                               MedBlankService medBlankService,
                               CandidateModelAssembler candidateModelAssembler,
                               MedBlankModelAssembler medBlankModelAssembler,
                               PagedResourcesAssembler<CandidateResponse> pagedCandidatesAssembler,
                               PagedResourcesAssembler<MedBlankResponse> pagedMedBlanksAssembler) {
        this.candidateService = candidateService;
        this.medBlankService = medBlankService;
        this.candidateModelAssembler = candidateModelAssembler;
        this.medBlankModelAssembler = medBlankModelAssembler;
        this.pagedCandidatesAssembler = pagedCandidatesAssembler;
        this.pagedMedBlanksAssembler = pagedMedBlanksAssembler;
    }

    @Override
    public PagedModel<EntityModel<CandidateResponse>> getAllCandidates(int page, int size) {
        PagedResponse<CandidateResponse> paged = candidateService.findAll(page, size);
        Page<CandidateResponse> springPage = new PageImpl<>(
                paged.content(),
                PageRequest.of(paged.pageNumber(), paged.pageSize()),
                paged.totalElements()
        );
        return pagedCandidatesAssembler.toModel(springPage, candidateModelAssembler);
    }

    @Override
    public EntityModel<CandidateResponse> getCandidateById(Long id) {
        return candidateModelAssembler.toModel(candidateService.findById(id));
    }

    @Override
    public ResponseEntity<EntityModel<CandidateResponse>> createCandidate(CandidateRequest request) {
        CandidateResponse created = candidateService.create(request);
        EntityModel<CandidateResponse> model = candidateModelAssembler.toModel(created);
        return ResponseEntity
                .created(model.getRequiredLink("self").toUri())
                .body(model);
    }

    @Override
    public EntityModel<CandidateResponse> updateCandidate(Long id, CandidateRequest request) {
        return candidateModelAssembler.toModel(candidateService.update(id, request));
    }

    @Override
    public EntityModel<CandidateResponse> patchCandidate(Long id, PatchCandidateRequest request) {
        return candidateModelAssembler.toModel(candidateService.patchCandidate(id, request));
    }

    @Override
    public void deleteCandidate(Long id) {
        candidateService.delete(id);
    }

    @Override
    public PagedModel<EntityModel<MedBlankResponse>> getMedBlanksByCandidate(Long id, int page, int size) {
        candidateService.findById(id);
        PagedResponse<MedBlankResponse> paged = medBlankService.findAllMedBlanks(id, null, null, page, size);
        Page<MedBlankResponse> springPage = new PageImpl<>(
                paged.content(),
                PageRequest.of(paged.pageNumber(), paged.pageSize()),
                paged.totalElements()
        );
        return pagedMedBlanksAssembler.toModel(springPage, medBlankModelAssembler);
    }
}
