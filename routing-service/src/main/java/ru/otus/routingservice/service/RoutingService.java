package ru.otus.routingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.otus.common.model.MatchingResult;
import ru.otus.routingservice.model.Context;

import java.net.URI;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoutingService {

    @Autowired
    private RestTemplate restTemplate;

    public String route(Context context) {
        log.info("Matching rules for " + context.getDevice() + " " + context.getBrowser() + " " + context.getRegion());

        URI rulesUri = UriComponentsBuilder.fromHttpUrl("http://localhost:8083/rules/match")
                .queryParam("device", context.getDevice().toLowerCase())
                .queryParam("browser", context.getBrowser().toLowerCase())
                .queryParam("region", context.getRegion().toLowerCase())
                .build()
                .encode()
                .toUri();

        log.info("Getting rules from uri " + rulesUri);

        MatchingResult result = restTemplate.getForObject(rulesUri, MatchingResult.class);

        log.info("MatchingResult " + result);

        if (result != null && !result.getUrls().isEmpty()) {
            return result.getUrls().get(0);
        }
        return "/default";
    }
}
