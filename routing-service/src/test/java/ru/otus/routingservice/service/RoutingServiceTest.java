package ru.otus.routingservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.otus.common.model.MatchingResult;
import ru.otus.routingservice.model.Context;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoutingServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RoutingService routingService;

    private Context validContext;

    @BeforeEach
    void setup() {
        validContext = Context.builder()
                .device("desktop")
                .browser("chrome")
                .region("ru")
                .build();
    }

    @Test
    void testRoute_SuccessfulScenario() {
        MatchingResult matchingResult = new MatchingResult(Arrays.asList("/first_url", "/second_url"));
        URI rulesUri = createUri(validContext);

        when(restTemplate.getForObject(rulesUri, MatchingResult.class)).thenReturn(matchingResult);

        String result = routingService.route(validContext);

        assertEquals("/first_url", result);
    }

    @Test
    void testRoute_NoMatchingRules() {
        MatchingResult matchingResult = new MatchingResult(Collections.emptyList());
        URI rulesUri = createUri(validContext);

        when(restTemplate.getForObject(rulesUri, MatchingResult.class)).thenReturn(matchingResult);

        String result = routingService.route(validContext);

        assertEquals("/default", result);
    }

    @Test
    void testRoute_EmptyParameter() {
        Context context = Context.builder()
                .device("")
                .browser("chrome")
                .region("ru")
                .build();

        MatchingResult matchingResult = new MatchingResult(Collections.emptyList());
        URI rulesUri = createUri(context);

        when(restTemplate.getForObject(rulesUri, MatchingResult.class)).thenReturn(matchingResult);

        String result = routingService.route(context);

        assertEquals("/default", result);
    }

    @Test
    void testRoute_LargeParameters() {
        String bigString = "X".repeat(255);

        Context context = Context.builder()
                .device(bigString)
                .browser(bigString)
                .region(bigString)
                .build();

        MatchingResult matchingResult = new MatchingResult(Arrays.asList("/big-data-url"));
        URI rulesUri = createUri(context);

        when(restTemplate.getForObject(rulesUri, MatchingResult.class)).thenReturn(matchingResult);

        String result = routingService.route(context);

        assertEquals("/big-data-url", result);
    }

    private URI createUri(Context context) {
        return UriComponentsBuilder.fromHttpUrl("http://localhost:8083/rules/match")
                .queryParam("device", context.getDevice().toLowerCase())
                .queryParam("browser", context.getBrowser().toLowerCase())
                .queryParam("region", context.getRegion().toLowerCase())
                .build()
                .encode()
                .toUri();
    }
}