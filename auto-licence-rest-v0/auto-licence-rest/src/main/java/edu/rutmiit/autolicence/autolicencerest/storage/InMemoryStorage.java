package edu.rutmiit.autolicence.autolicencerest.storage;


import edu.rutmiit.candidateapi.candidateapicontract.dto.MedBlankResponse;
import edu.rutmiit.candidateapi.candidateapicontract.dto.CandidateResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryStorage {
    public final Map<Long, CandidateResponse> candidates = new ConcurrentHashMap<>();
    public final Map<Long, MedBlankResponse> medBlanks = new ConcurrentHashMap<>();

    public final AtomicLong candidateSequence = new AtomicLong(0);
    public final AtomicLong medBlankSequence = new AtomicLong(0);

    @PostConstruct
    public void init() {
        CandidateResponse candidate1 = CandidateResponse.builder()
                .id(candidateSequence.incrementAndGet())
                .firstName("Денис")
                .lastName("Кириенко")
                .patronymicName("Петрович")
                .phoneNumber("+7(925)345-19-18")
                .passportData("4618 213288")
                .currentLicenceRevocation("Нет")
                .licenceCategory("M")
                .build();


        candidates.put(candidate1.getId(), candidate1);

        long medBlankId1 = medBlankSequence.incrementAndGet();
        medBlanks.put(medBlankSequence.incrementAndGet(), MedBlankResponse.builder()

                .blankCode("MOS_GOV-CLINIC1724_123714091467")
                .candidate(candidate1)
                .omsId("743675854595")
                .eyeCheck("Зрение 0. Патологий нет")
                .psyhicCheck("Стабильное психологическое состояние. Аутизм")
                .nervousSystemCheck("Незначительное поверждение вегетативной нервной системы")
                .candidateHealthCheckResult("Пациент не имеет критических противопоказаний для становления водителем")
                .build());


    }
}