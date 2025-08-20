package ru.otus.rulemanagementservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.rulemanagementservice.model.RouteRule;

public interface RouteRuleRepository extends JpaRepository<RouteRule, Long>, JpaSpecificationExecutor<RouteRule> {}
