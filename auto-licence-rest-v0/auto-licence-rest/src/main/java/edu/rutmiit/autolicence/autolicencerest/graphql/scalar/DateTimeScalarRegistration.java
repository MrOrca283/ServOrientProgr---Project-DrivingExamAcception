package edu.rutmiit.autolicence.autolicencerest.graphql.scalar;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsRuntimeWiring;
import graphql.scalars.ExtendedScalars;
import graphql.schema.idl.RuntimeWiring;

/**
 * Аннотация @DgsRuntimeWiring позволяет программно настроить runtime wiring —
 * механизм GraphQL-Java для связывания схемы с реализацией.
 */
@DgsComponent
public class DateTimeScalarRegistration {

    @DgsRuntimeWiring
    public RuntimeWiring.Builder addScalars(RuntimeWiring.Builder builder) {
        return builder
                .scalar(ExtendedScalars.DateTime)
                .scalar(ExtendedScalars.Date);
    }
}
