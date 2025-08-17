package ru.otus.routingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.routingservice.model.Context;
import ru.otus.routingservice.model.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoutingService {

    // tbd
    Rule rule = Rule.builder()
            .id(1L)
            .condition("device=iPhone")
            .destinationURL("/iphone-dashboard")
            .build();

    Rule rule2 = Rule.builder()
            .id(2L)
            .condition("device=Android")
            .destinationURL("/android-dashboard")
            .build();

    Rule rule3 = Rule.builder()
            .id(2L)
            .condition("device=DESKTOP")
            .destinationURL("/android-dashboard")
            .build();

    private List<Rule> rules = Arrays.asList(rule, rule2, rule3);


    public RoutingService(List<Rule> rules) {
        this.rules = rules;
    }

    public String route(Context context) {
        log.info("Context : " + context.toString());
        for (Rule rule : rules) {
            if (matchRule(rule.getCondition(), context)) {
                log.info("!!!Running rule condition check!!!");
                return rule.getDestinationURL();
            }
        }
        return "/default";
    }

    private boolean matchRule(String condition, Context context) {
        try {
            // Разделяем сложное условие на отдельные части по оператору 'AND'
            String[] conditions = condition.split("\\s*AND\\s*");

            for (String cond : conditions) {
                int equalsIndex = cond.indexOf('=');

                if (equalsIndex <= 0 || equalsIndex >= cond.length()) {
                    throw new IllegalArgumentException("Неправильный формат условия: '" + cond + "'");
                }

                String fieldName = cond.substring(0, equalsIndex).trim();
                String valueStr = cond.substring(equalsIndex + 1).replaceAll("^['\"]|['\"]$", "").trim(); // Удаляем кавычки

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
    }
}
