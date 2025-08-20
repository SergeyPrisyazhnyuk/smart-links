package ru.otus.rulemanagementservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.common.model.MatchingResult;
import ru.otus.rulemanagementservice.model.RouteRule;
import ru.otus.rulemanagementservice.model.RouteUrl;
import ru.otus.rulemanagementservice.repository.RouteRuleRepository;
import ru.otus.rulemanagementservice.repository.RouteUrlRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuleService {

    @Autowired
    private final RouteUrlRepository routeUrlRepository;

    @Autowired
    private final RouteRuleRepository routeRuleRepository;

    public MatchingResult match(String device, String browser, String region) {
        log.info("Matching rules for " + device + " " + browser + " " + region);

        List<RouteRule> matchingRules = findMatchingRules(device, browser, region);

        Set<Long> ruleIds = matchingRules.stream().map(RouteRule::getId).collect(Collectors.toSet());

        List<RouteUrl> relatedURLs = routeUrlRepository.findByRouteRuleIdIn(ruleIds);
        log.info("Found " + relatedURLs.size() + " related URLs");

        List<String> urls = relatedURLs.stream()
                .map(RouteUrl::getDestinationURL)
                .collect(Collectors.toList());

        MatchingResult matchingResult = new MatchingResult(urls);

        log.info("MatchingResult " + matchingResult);

        return matchingResult;
    }

    public List<RouteRule> findMatchingRules(String device, String browser, String region) {
        List<RouteRule> results = routeRuleRepository.findAll(RouteRuleService.findExactMatchingRules(device, browser, region));
        return results;
    }


    public List<RouteUrl> listRules() {
        return routeUrlRepository.findAll();
    }

    public RouteUrl createRule(RouteUrl rule) {
        return routeUrlRepository.save(rule);
    }

    public RouteUrl updateRule(Long id, RouteUrl updatedRule) {
        updatedRule.setId(id);
        return routeUrlRepository.save(updatedRule);
    }

    public void deleteRule(Long id) {
        routeUrlRepository.deleteById(id);
    }
}
