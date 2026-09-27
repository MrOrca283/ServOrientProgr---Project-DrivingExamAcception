package edu.rutmiit.autolicence.autolicencerest.assemblers;

import edu.rutmiit.autolicence.autolicencerest.controllers.CandidateController;
import edu.rutmiit.autolicence.autolicencerest.controllers.MedBlankController;
import edu.rutmiit.candidateapi.candidateapicontract.dto.CandidateResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CandidateModelAssembler implements RepresentationModelAssembler<CandidateResponse, EntityModel<CandidateResponse>> {

    @Override
    public EntityModel<CandidateResponse> toModel(CandidateResponse candidate) {
        return EntityModel.of(candidate,
                linkTo(methodOn(CandidateController.class).getCandidateById(candidate.getId())).withSelfRel(),
                linkTo(methodOn(MedBlankController.class).getAllMedBlanks(candidate.getId(), null, null, 0, 20)).withRel("medBlanks"),
                linkTo(methodOn(CandidateController.class).getAllCandidates(0, 20)).withRel("collection")
        );
    }
}