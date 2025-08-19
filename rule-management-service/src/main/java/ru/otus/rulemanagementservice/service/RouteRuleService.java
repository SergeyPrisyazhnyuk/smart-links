package ru.otus.rulemanagementservice.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.rulemanagementservice.model.RouteRule;

import java.util.ArrayList;
import java.util.List;

public class RouteRuleService {

    public static Specification<RouteRule> findMatchingRules(final String device, final String browser, final String region) {
        return (Root<RouteRule> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Predicate allFieldsMatch = cb.and(cb.equal(root.get("device"), device),
                    cb.equal(root.get("browser"), browser),
                    cb.equal(root.get("region"), region));

            Predicate twoFieldsMatch = cb.and(cb.equal(root.get("device"), device),
                    cb.equal(root.get("browser"), browser));

            Predicate oneFieldMatch = cb.equal(root.get("device"), device);

            predicates.add(allFieldsMatch);
            predicates.add(twoFieldsMatch);
            predicates.add(oneFieldMatch);

            return cb.or(predicates.toArray(Predicate[]::new));
        };
    }
}
