package ru.otus.rulemanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.common.model.MatchingResult;
import ru.otus.rulemanagementservice.model.RouteRule;
import ru.otus.rulemanagementservice.model.RouteUrl;
import ru.otus.rulemanagementservice.repository.RouteRuleRepository;
import ru.otus.rulemanagementservice.repository.RuleUrlRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RuleService {

    @Autowired
    private final RuleUrlRepository ruleRepository;

    @Autowired
    private final RouteRuleRepository repository;

    public MatchingResult match(String device, String browser, String region) {
        // Шаг 1: Найдите все правила, совпадающие по входящим параметрам
        List<RouteRule> matchingRules = findMatchingRules(device, browser, region);

        // Шаг 2: Извлечь связанные URL
        Set<Long> ruleIds = matchingRules.stream().map(RouteRule::getId).collect(Collectors.toSet());

        // Получаем все URL, связанные с этими правилами
        List<RouteUrl> relatedURLs = routeUrlRepository.findByRouteRuleIdIn(ruleIds);

        // Шаг 3: Сформируйте список URL
        List<String> urls = relatedURLs.stream()
                .map(RouteUrl::getDestinationURL)
                .collect(Collectors.toList());

        // Шаг 4: Верните результат
        return new MatchingResult(urls);
    }
    public List<RouteRule> findMatchingRules(String device, String browser, String region) {

        List<RouteRule> results = repository.findAll(RouteRuleService.findMatchingRules(device, browser, region));

        return results;
    }


    public List<RouteUrl> listRules() {
        return ruleRepository.findAll();
    }

    public RouteUrl createRule(RouteUrl rule) {
        return ruleRepository.save(rule);
    }

    public RouteUrl updateRule(Long id, RouteUrl updatedRule) {
        updatedRule.setId(id);
        return ruleRepository.save(updatedRule);
    }

    public void deleteRule(Long id) {
        ruleRepository.deleteById(id);
    }
}
