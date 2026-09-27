package edu.rutmiit.eventing.events;


public sealed interface MedBlankEvent {


    record Created(
            Long candidateId,
            String blankCode,
            String omsId
    ) implements MedBlankEvent {}


    record Updated(
            Long candidateId,
            String blankCode,
            String omsId
    ) implements MedBlankEvent {}


    record Deleted(
            String blankCode
    ) implements MedBlankEvent {}


    record Enriched(
            Long MedBlankId,
            Long candidateId,
            String blankCode,
            String eyeCheck,
            String psyhicCheck,
            String nervousSystemCheck,
            String candidateHealthCheckResults
    ) implements MedBlankEvent {}
}
