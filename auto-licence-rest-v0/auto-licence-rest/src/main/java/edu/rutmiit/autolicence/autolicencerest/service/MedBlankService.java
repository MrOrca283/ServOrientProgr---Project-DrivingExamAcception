package edu.rutmiit.autolicence.autolicencerest.service;


import edu.rutmiit.autolicence.autolicencerest.event.MedBlankEventPublisher;
import edu.rutmiit.autolicence.autolicencerest.storage.InMemoryStorage;
import edu.rutmiit.candidateapi.candidateapicontract.dto.*;
import edu.rutmiit.candidateapi.candidateapicontract.exception.BlankCodeAlreadyExistsException;
import edu.rutmiit.candidateapi.candidateapicontract.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class MedBlankService {

    private final InMemoryStorage storage;
    private final CandidateService candidateService;
    private final MedBlankEventPublisher eventPublisher;

    public MedBlankService(InMemoryStorage storage,
                           @Lazy CandidateService candidateService,
                           MedBlankEventPublisher eventPublisher) {
        this.storage = storage;
        this.candidateService = candidateService;
        this.eventPublisher = eventPublisher;
    }

    public MedBlankResponse findMedBlankById(Long id) {
        return Optional.ofNullable(storage.medBlanks.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("MedBlank", id));
    }

    public PagedResponse<MedBlankResponse> findAllMedBlanks(Long candidateId, String omsId,
                                                            String blankCodeSearch, int page, int size) {
        Stream<MedBlankResponse> stream = storage.medBlanks.values().stream()
                .sorted((b1, b2) -> b1.getMedBlankId().compareTo(b2.getMedBlankId()));

        if (candidateId != null) {
            stream = stream.filter(b -> b.getCandidate() != null && b.getCandidate().getId().equals(candidateId));
        }
        if (blankCodeSearch != null && !blankCodeSearch.isBlank()) {
            stream = stream.filter(b -> b.getBlankCode() != null && b.getBlankCode().contains(blankCodeSearch));
        }

        List<MedBlankResponse> allMedBlanks = stream.toList();
        int totalElements = allMedBlanks.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<MedBlankResponse> content = (from >= totalElements) ? List.of() : allMedBlanks.subList(from, to);
        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    public MedBlankResponse createMedBlank(MedBlankRequest request) {
        validateMedBlankCode(request.blankCode(), null);
        CandidateResponse candidate = candidateService.findById(Long.valueOf(request.candidateId()));

        long id = storage.medBlankSequence.incrementAndGet();
        MedBlankResponse medBlank = MedBlankResponse.builder()
                .medBlankId(id)
                .blankCode(request.blankCode())
                .candidate(candidate)
                .omsId(request.omsId())
                .eyeCheck(request.eyeCheck())
                .psyhicCheck(request.psyhicCheck())
                .nervousSystemCheck(request.nervousSystemCheck())
                .candidateHealthCheckResult(request.candidateHealthCheckResult())
                .build();
        storage.medBlanks.put(id, medBlank);

        // Публикуем доменное событие ПОСЛЕ успешного сохранения.
        // Если RabbitMQ недоступен — книга всё равно создана, событие просто потеряется.
        eventPublisher.publishCreated(medBlank);

        return medBlank;
    }

    public MedBlankResponse updateMedBlank(Long id, UpdateMedBlankRequest request) {
        MedBlankResponse existing = findMedBlankById(id);
        validateMedBlankCode(request.blankCode(), id);

        MedBlankResponse updated = MedBlankResponse.builder()
                .medBlankId(id)
                .blankCode(request.blankCode())
                .omsId(request.omsId())
                .candidate(existing.getCandidate())
                .eyeCheck(request.eyeCheck())
                .psyhicCheck(request.psyhicCheck())
                .nervousSystemCheck(request.nervousSystemCheck())
                .candidateHealthCheckResult(request.candidateHealthCheckResult())
                .build();
        storage.medBlanks.put(id, updated);
        eventPublisher.publishUpdated(updated);
        return updated;
    }

    public MedBlankResponse patchMedBlank(Long id, PatchMedBlankRequest request) {
        MedBlankResponse existing = findMedBlankById(id);

        MedBlankResponse updated = MedBlankResponse.builder()
                .medBlankId(id)
                .blankCode(request.blankCode() != null ? request.blankCode() : existing.getBlankCode())
                .candidate(existing.getCandidate())
                .eyeCheck(request.eyeCheck() != null ? request.eyeCheck() : existing.getEyeCheck())
                .psyhicCheck(request.psyhicCheck() != null ? request.psyhicCheck() : existing.getPsyhicCheck())
                .nervousSystemCheck(request.nervousSystemCheck() != null ? request.nervousSystemCheck() : existing.getNervousSystemCheck())
                .candidateHealthCheckResult(request.candidateHealthCheckResult() != null ? request.candidateHealthCheckResult() : existing.getCandidateHealthCheckResult())
                .build();
        storage.medBlanks.put(id, updated);
        eventPublisher.publishUpdated(updated);
        return updated;
    }

    public void deleteMedBlank(Long id) {
        MedBlankResponse medBlank = findMedBlankById(id);
        storage.medBlanks.remove(id);
        eventPublisher.publishDeleted(medBlank.getBlankCode());
    }

    public void deleteMedBlanksByCandidateId(Long candidateId) {
        List<Long> toDelete = storage.medBlanks.values().stream()
                .filter(b -> b.getCandidate() != null && b.getCandidate().getId().equals(candidateId))
                .map(MedBlankResponse::getMedBlankId)
                .toList();
        toDelete.forEach(storage.medBlanks::remove);
    }

    private void validateMedBlankCode(String blankCode, Long currentMedBlankId) {
        storage.medBlanks.values().stream()
                .filter(b -> b.getBlankCode().equalsIgnoreCase(blankCode))
                .filter(b -> !b.getMedBlankId().equals(currentMedBlankId))
                .findAny()
                .ifPresent(b -> { throw new BlankCodeAlreadyExistsException(blankCode); });
    }
}
