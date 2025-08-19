package ru.otus.rulemanagementservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.rulemanagementservice.model.RouteUrl;

import java.util.List;
import java.util.Set;

public interface RuleUrlRepository extends JpaRepository<RouteUrl, Long> {
    List<RouteUrl> findByRouteRuleIdIn(Set<Long> ruleIds);

}