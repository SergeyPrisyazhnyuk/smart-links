package ru.otus.rulemanagementservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.common.model.MatchingResult;
import ru.otus.rulemanagementservice.model.RouteUrl;
import ru.otus.rulemanagementservice.service.RuleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rules")
public class RuleController {

    @Autowired
    private final RuleService ruleService;

/*    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }*/

    @GetMapping("/match")
    public MatchingResult matchRoutes(@RequestParam(required=false) String device,
                                      @RequestParam(required=false) String browser,
                                      @RequestParam(required=false) String region) {
        return ruleService.match(device, browser, region);
    }

    @GetMapping
    public List<RouteUrl> listRules() {
        return ruleService.listRules();
    }

    @PostMapping
    public RouteUrl createRule(@RequestBody RouteUrl rule) {
        return ruleService.createRule(rule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteUrl> updateRule(@PathVariable Long id, @RequestBody RouteUrl updatedRule) {
        RouteUrl updated = ruleService.updateRule(id, updatedRule);
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
