package edu.rutmiit.autolicence.autolicencerest.graphql.security;

import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Для защиты используются Instrumentation перехватывать запрос на этапе валидации и отклонять его
 * до выполнения.
 */
@Configuration
public class GraphQLSecurityConfig {

    /**
     * Максимальная глубина вложенности запроса (20 уровней).
     * Значение 20 защищает от рекурсии.
     */
    @Bean
    public Instrumentation maxQueryDepthInstrumentation() {
        return new MaxQueryDepthInstrumentation(20);
    }

    @Bean
    public Instrumentation maxQueryComplexityInstrumentation() {
        return new MaxQueryComplexityInstrumentation(200);
    }
}
