package ru.otus.rulemanagementservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.rulemanagementservice.model.Rule;
import ru.otus.rulemanagementservice.service.RuleService;

import java.util.List;

@RestController
@RequestMapping("/rules")
public class RuleController {

    private final RuleService ruleService;

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @GetMapping
    public List<Rule> listRules() {
        return ruleService.listRules();
    }

    @PostMapping
    public Rule createRule(@RequestBody Rule rule) {
        return ruleService.createRule(rule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rule> updateRule(@PathVariable Long id, @RequestBody Rule updatedRule) {
        Rule updated = ruleService.updateRule(id, updatedRule);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        try {
            ruleService.deleteRule(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            if (ex.getMessage().contains("Rule not found")) {
                return ResponseEntity.notFound().build();
            }
            throw ex;
        }
    }
}
