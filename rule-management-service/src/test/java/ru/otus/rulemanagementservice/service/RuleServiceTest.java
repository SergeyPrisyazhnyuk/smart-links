package ru.otus.rulemanagementservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.rulemanagementservice.model.Rule;
import ru.otus.rulemanagementservice.repository.RuleRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RuleServiceTest {

    @InjectMocks
    private RuleService ruleService;

    @Mock
    private RuleRepository ruleRepository;

    @Test
    public void listRulesReturnsAllRulesTest() {
        List<Rule> expectedRules = Arrays.asList(
                new Rule(1L, "device='iPhone'", "/iphone-dashboard"),
                new Rule(2L, "device='Android'", "/android-dashboard")
        );

        when(ruleRepository.findAll()).thenReturn(expectedRules);

        List<Rule> actualRules = ruleService.listRules();

        assertEquals(expectedRules.size(), actualRules.size());
        assertTrue(actualRules.containsAll(expectedRules));
    }

    @Test
    public void createRuleSavesNewRuleTest() {
        Rule newRule = new Rule(null, "device='Windows'", "/windows-dashboard");

        when(ruleRepository.save(any(Rule.class))).thenAnswer(invocation -> {
            Rule rule = invocation.getArgument(0);
            rule.setId(1L);
            return rule;
        });

        Rule savedRule = ruleService.createRule(newRule);

        assertNotNull(savedRule.getId());
        assertEquals(newRule.getCondition(), savedRule.getCondition());
        assertEquals(newRule.getDestinationURL(), savedRule.getDestinationURL());
    }

    @Test
    public void updateRuleUpdatesExistingRuleTest() {
        Rule oldRule = new Rule(1L, "device='iPhone'", "/iphone-dashboard");
        Rule updatedRule = new Rule(1L, "device='Mac'", "/mac-dashboard");

        when(ruleRepository.save(any(Rule.class))).thenAnswer(i -> i.getArguments()[0]);

        Rule result = ruleService.updateRule(oldRule.getId(), updatedRule);

        assertEquals(result.getId(), updatedRule.getId());
        assertEquals(result.getCondition(), updatedRule.getCondition());
        assertEquals(result.getDestinationURL(), updatedRule.getDestinationURL());
    }

    @Test
    public void deleteRuleDeletesRuleByIdTest() {
        Long ruleId = 1L;

        doNothing().when(ruleRepository).deleteById(ruleId);

        ruleService.deleteRule(ruleId);

        verify(ruleRepository, times(1)).deleteById(ruleId);
    }

}
