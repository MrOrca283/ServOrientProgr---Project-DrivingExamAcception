package edu.rutmiit.demo.auditservice.model;

import java.time.Instant;



public record AuditEntry(
        long sequenceNumber,
        String eventType,
        String eventId,
        String source,
        Instant eventTimestamp,
        Instant receivedAt,
        String description
) {}
