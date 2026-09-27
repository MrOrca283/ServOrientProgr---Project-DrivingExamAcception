package edu.rutmiit.eventing.events;


public sealed interface CandidateEvent {


    record Created(
            Long id,
            String fullName
    ) implements CandidateEvent {}

    record Deleted(
            Long id,
            String fullName
    ) implements CandidateEvent {}
}
