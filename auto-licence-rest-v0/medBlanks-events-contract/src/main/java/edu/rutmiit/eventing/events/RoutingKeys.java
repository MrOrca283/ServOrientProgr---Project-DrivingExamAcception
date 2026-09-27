package edu.rutmiit.eventing.events;

public final class RoutingKeys {

    private RoutingKeys() {
    }


    public static final String EXCHANGE = "medBlanks.events";

    public static final String MEDBLANK_CREATED = "medBlank.created";
    public static final String MEDBLANK_UPDATED = "medBlank.updated";
    public static final String MEDBLANK_DELETED = "medBlank.deleted";
    public static final String MEDBLANK_ENRICHED = "medBlank.enriched";


    public static final String CANDIDATE_CREATED = "candidate.created";
    public static final String CANDIDATE_DELETED = "candidate.deleted";


    public static final String ALL_MEDBLANK_EVENTS = "medBlank.*";
    public static final String ALL_CANDIDATE_EVENTS = "candidate.*";
    public static final String ALL_EVENTS = "#";
}
