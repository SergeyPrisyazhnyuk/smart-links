package ru.otus.rulemanagementservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.rulemanagementservice.model.Rule;

public interface RuleRepository extends JpaRepository<Rule, Long> {}