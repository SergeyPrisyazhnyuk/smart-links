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
import java.util.Arrays;
import java.util.Objects;

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

/*    private boolean matchRule(String condition, Context context) {
        try {
            log.info("!!!! context.toString() : " + context.toString());
            log.info("!!!! Condition : " + condition);
            String[] conditions = condition.split("\\s*AND\\s*");
            log.info("!!!! Conditions : " + Arrays.toString(conditions));

            for (String cond : conditions) {
                int equalsIndex = cond.indexOf('=');

                if (equalsIndex <= 0 || equalsIndex >= cond.length()) {
                    throw new IllegalArgumentException("Неправильный формат условия: '" + cond + "'");
                }

                String fieldName = cond.substring(0, equalsIndex).trim();
                String valueStr = cond.substring(equalsIndex + 1).replaceAll("^['\"]|['\"]$", "").trim();
                log.info("!!!! fieldName : " + fieldName);
                log.info("!!!! valueStr : " + valueStr);


                switch (fieldName.toLowerCase()) {
                    case "device":
                        if (!Objects.equals(valueStr, context.getDevice())) {
                            return false;
                        }
                        break;
                    case "browser":
                        if (!Objects.equals(valueStr, context.getBrowser())) {
                            return false;
                        }
                        break;
                    case "region":
                        if (!Objects.equals(valueStr, context.getRegion())) {
                            return false;
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Неверное имя поля: '" + fieldName + "'");
                }
            }

            return true;
        } catch (Exception e) {
            System.err.println("Ошибка обработки условия '" + condition + "': " + e.getMessage());
            return false;
        }
    }*/
}
