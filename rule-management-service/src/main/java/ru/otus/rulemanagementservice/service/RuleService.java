package ru.otus.rulemanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.rulemanagementservice.model.Rule;
import ru.otus.rulemanagementservice.repository.RuleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RuleService {

    private final RuleRepository ruleRepository;

    public List<Rule> listRules() {
        return ruleRepository.findAll();
    }

    public Rule createRule(Rule rule) {
        return ruleRepository.save(rule);
    }

    public Rule updateRule(Long id, Rule updatedRule) {
        updatedRule.setId(id);
        return ruleRepository.save(updatedRule);
    }

    public void deleteRule(Long id) {
        ruleRepository.deleteById(id);
    }
}
