package ru.otus.rulemanagementservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.common.model.MatchingResult;
import ru.otus.rulemanagementservice.model.RouteRule;
import ru.otus.rulemanagementservice.model.RouteUrl;
import ru.otus.rulemanagementservice.repository.RouteRuleRepository;
import ru.otus.rulemanagementservice.repository.RouteUrlRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RuleManagementServiceTest {

    @Mock
    private RouteUrlRepository routeUrlRepository;

    @Mock
    private RouteRuleRepository routeRuleRepository;

    @InjectMocks
    private RuleManagementService ruleManagementService;

    private List<RouteRule> fakeRouteRules;
    private List<RouteUrl> fakeRouteUrls;

    @BeforeEach
    void setUp() {
        fakeRouteRules = new ArrayList<>();
        fakeRouteUrls = new ArrayList<>();

        RouteRule rule1 = new RouteRule();
        rule1.setId(1L);
        rule1.setDevice("Android");
        rule1.setBrowser("Chrome");
        rule1.setRegion("RU");
        fakeRouteRules.add(rule1);

        RouteUrl url1 = new RouteUrl();
        url1.setId(1L);
        url1.setDestinationURL("https://example.com/android-chrome-ru");
        url1.setRouteRuleId(1L);
        fakeRouteUrls.add(url1);
    }

    @Test
    void testMatch() {
        when(routeRuleRepository.findAll(any(Specification.class))).thenReturn(fakeRouteRules);
        when(routeUrlRepository.findByRouteRuleIdIn(Set.of(1L))).thenReturn(fakeRouteUrls);

        MatchingResult result = ruleManagementService.match("Android", "Chrome", "RU");

        assertEquals(1, result.getUrls().size());
        assertEquals("https://example.com/android-chrome-ru", result.getUrls().get(0));
    }

    @Test
    void testMatch_NoMatches() {
        when(routeRuleRepository.findAll(any(Specification.class))).thenReturn(List.of());
        when(routeUrlRepository.findByRouteRuleIdIn(Set.of())).thenReturn(List.of());

        MatchingResult result = ruleManagementService.match("iPhone", "Safari", "US");

        assertEquals(0, result.getUrls().size());
    }

    @Test
    void testListRules() {
        when(routeUrlRepository.findAll()).thenReturn(fakeRouteUrls);

        List<RouteUrl> result = ruleManagementService.listRules();

        assertEquals(1, result.size());
        assertEquals("https://example.com/android-chrome-ru", result.get(0).getDestinationURL());
    }

    @Test
    void testCreateRule() {
        RouteUrl newRule = new RouteUrl();
        newRule.setDestinationURL("https://example.com/new-rule");
        when(routeUrlRepository.save(newRule)).thenReturn(newRule);

        RouteUrl createdRule = ruleManagementService.createRule(newRule);

        assertEquals("https://example.com/new-rule", createdRule.getDestinationURL());
    }

    @Test
    void testUpdateRule() {
        RouteUrl existingRule = new RouteUrl();
        existingRule.setId(1L);
        existingRule.setDestinationURL("https://example.com/update-test");
        when(routeUrlRepository.save(existingRule)).thenReturn(existingRule);

        RouteUrl updatedRule = ruleManagementService.updateRule(1L, existingRule);

        assertEquals("https://example.com/update-test", updatedRule.getDestinationURL());
    }

    @Test
    void testDeleteRule() {
        RouteUrl ruleToDelete = new RouteUrl();
        ruleToDelete.setId(1L);
        ruleToDelete.setDestinationURL("https://example.com/delete-me");

        ruleManagementService.deleteRule(1L);
    }
}