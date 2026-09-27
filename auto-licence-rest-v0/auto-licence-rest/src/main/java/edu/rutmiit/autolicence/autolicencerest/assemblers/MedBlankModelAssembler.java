package edu.rutmiit.autolicence.autolicencerest.assemblers;

import edu.rutmiit.autolicence.autolicencerest.controllers.CandidateController;
import edu.rutmiit.autolicence.autolicencerest.controllers.MedBlankController;
import edu.rutmiit.candidateapi.candidateapicontract.dto.MedBlankResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MedBlankModelAssembler implements RepresentationModelAssembler<MedBlankResponse, EntityModel<MedBlankResponse>> {

    @Override
    public EntityModel<MedBlankResponse> toModel(MedBlankResponse medBlank) {
        EntityModel<MedBlankResponse> model = EntityModel.of(medBlank,
                linkTo(methodOn(MedBlankController.class).getMedBlankById(medBlank.getMedBlankId())).withSelfRel(),
                linkTo(methodOn(MedBlankController.class).getAllMedBlanks(null, null, null, 0, 20)).withRel("collection")
        );
        if (medBlank.getCandidate() != null) {
            model.add(linkTo(methodOn(CandidateController.class)
                    .getCandidateById(medBlank.getCandidate().getId())).withRel("candidate"));
        }
        return model;
    }
}
