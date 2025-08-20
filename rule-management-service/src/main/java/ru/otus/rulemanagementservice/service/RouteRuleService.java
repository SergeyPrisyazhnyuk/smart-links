package ru.otus.rulemanagementservice.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.rulemanagementservice.model.RouteRule;

public class RouteRuleService {

    public static Specification<RouteRule> findExactMatchingRules(final String device, final String browser, final String region) {
        return (Root<RouteRule> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate exactMatchPredicate = cb.and(
                    cb.equal(root.get("device"), device),
                    cb.equal(root.get("browser"), browser),
                    cb.equal(root.get("region"), region)
            );

            return exactMatchPredicate;
        };
    }

}
