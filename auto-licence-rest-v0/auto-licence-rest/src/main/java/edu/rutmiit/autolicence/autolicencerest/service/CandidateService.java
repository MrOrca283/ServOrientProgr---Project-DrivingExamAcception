package edu.rutmiit.autolicence.autolicencerest.service;

import edu.rutmiit.autolicence.autolicencerest.event.CandidateEventPublisher;
import edu.rutmiit.autolicence.autolicencerest.storage.InMemoryStorage;
import edu.rutmiit.candidateapi.candidateapicontract.dto.CandidateRequest;
import edu.rutmiit.candidateapi.candidateapicontract.dto.CandidateResponse;
import edu.rutmiit.candidateapi.candidateapicontract.dto.PagedResponse;
import edu.rutmiit.candidateapi.candidateapicontract.dto.PatchCandidateRequest;
import edu.rutmiit.candidateapi.candidateapicontract.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {
    private final InMemoryStorage storage;
    private final MedBlankService medBlankService;
    private final CandidateEventPublisher eventPublisher;

    public CandidateService(InMemoryStorage storage,
                            @Lazy MedBlankService medBlankService,
                            CandidateEventPublisher eventPublisher) {
        this.storage = storage;
        this.medBlankService = medBlankService;
        this.eventPublisher = eventPublisher;
    }

    public PagedResponse<CandidateResponse> findAll(int page, int size) {
        List<CandidateResponse> all = storage.candidates.values().stream()
                .sorted(Comparator.comparingLong(CandidateResponse::getId))
                .toList();
        int totalElements = all.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<CandidateResponse> content = (from >= totalElements) ? List.of() : all.subList(from, to);
        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    public CandidateResponse findById(Long id) {
        return Optional.ofNullable(storage.candidates.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", id));
    }

    public CandidateResponse create(CandidateRequest request) {
        long id = storage.candidateSequence.incrementAndGet();
        String fullName =request.lastName() +" "+ request.firstName() + " " +request.patronymicName();
        CandidateResponse candidate = CandidateResponse.builder()
                .id(id)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .patronymicName(request.patronymicName())
                .fullName(fullName)
                .phoneNumber(request.phoneNumber())
                .passportData(request.passportData())
                .currentLicenceRevocation(request.currentLicenceRevocation())
                .licenceCategory(request.licenceCategory())
                .build();
        storage.candidates.put(id, candidate);
        eventPublisher.publishCreated(candidate);
        return candidate;
    }

    public CandidateResponse update(Long id, CandidateRequest request) {
        CandidateResponse existing = findById(id);
        String fullName = request.firstName() + " " + request.lastName();
        CandidateResponse updatedCandidate = CandidateResponse.builder()
                .id(id)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .patronymicName(request.patronymicName())
                .fullName(fullName)
                .phoneNumber(request.phoneNumber())
                .passportData(request.passportData())
                .currentLicenceRevocation(request.currentLicenceRevocation())
                .licenceCategory(request.licenceCategory())
                .build();
        storage.candidates.put(id, updatedCandidate);
        return updatedCandidate;
    }

    public CandidateResponse patchCandidate(Long id, PatchCandidateRequest request) {
        CandidateResponse existing = findById(id);
        String newFirstName = request.firstName() != null ? request.firstName() : existing.getFirstName();
        String newPatronymicName=request.patronymicName()!=null?request.patronymicName():existing.getPatronymicName();
        String newLastName = request.lastName() != null ? request.lastName() : existing.getLastName();
        CandidateResponse updated = CandidateResponse.builder()
                .id(id)
                .firstName(newFirstName)
                .lastName(newLastName)
                .patronymicName(newPatronymicName)
                .fullName(newFirstName + " " + newLastName)
                .phoneNumber(request.phoneNumber() != null ? request.phoneNumber() : existing.getPhoneNumber())
                .passportData(request.passportData() != null ? request.passportData() : existing.getPassportData())
                .currentLicenceRevocation(request.currentLicenceRevocation() != null ? request.currentLicenceRevocation() : existing.getCurrentLicenceRevocation())
                .licenceCategory(request.licenceCategory() != null ? request.licenceCategory() : existing.getLicenceCategory())
                .build();
        storage.candidates.put(id, updated);
        return updated;
    }

    public void delete(Long id) {
        CandidateResponse candidate = findById(id);

        // Считаем книги до каскадного удаления — для события аудита
        int medBlanksCount = (int) storage.medBlanks.values().stream()
                .filter(b -> b.getCandidate() != null && b.getCandidate().getId().equals(id))
                .count();

        medBlankService.deleteMedBlanksByCandidateId(id);
        storage.candidates.remove(id);
        eventPublisher.publishDeleted(candidate, medBlanksCount);
    }
}
