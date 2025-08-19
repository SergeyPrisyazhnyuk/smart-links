package ru.otus.rulemanagementservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.rulemanagementservice.model.RouteUrl;
import ru.otus.rulemanagementservice.repository.RuleUrlRepository;

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
    private RuleUrlRepository ruleRepository;

    @Test
    public void listRulesReturnsAllRulesTest() {
        List<RouteUrl> expectedRules = Arrays.asList(
                new RouteUrl(1L, "device='iPhone'", "/iphone-dashboard"),
                new RouteUrl(2L, "device='Android'", "/android-dashboard")
        );

        when(ruleRepository.findAll()).thenReturn(expectedRules);

        List<RouteUrl> actualRules = ruleService.listRules();

        assertEquals(expectedRules.size(), actualRules.size());
        assertTrue(actualRules.containsAll(expectedRules));
    }

    @Test
    public void createRuleSavesNewRuleTest() {
        RouteUrl newRule = new RouteUrl(null, "device='Windows'", "/windows-dashboard");

        when(ruleRepository.save(any(RouteUrl.class))).thenAnswer(invocation -> {
            RouteUrl rule = invocation.getArgument(0);
            rule.setId(1L);
            return rule;
        });

        RouteUrl savedRule = ruleService.createRule(newRule);

        assertNotNull(savedRule.getId());
        assertEquals(newRule.getCondition(), savedRule.getCondition());
        assertEquals(newRule.getDestinationURL(), savedRule.getDestinationURL());
    }

    @Test
    public void updateRuleUpdatesExistingRuleTest() {
        RouteUrl oldRule = new RouteUrl(1L, "device='iPhone'", "/iphone-dashboard");
        RouteUrl updatedRule = new RouteUrl(1L, "device='Mac'", "/mac-dashboard");

        when(ruleRepository.save(any(RouteUrl.class))).thenAnswer(i -> i.getArguments()[0]);

        RouteUrl result = ruleService.updateRule(oldRule.getId(), updatedRule);

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
