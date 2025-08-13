package ru.otus.routingservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.routingservice.model.Context;
import ru.otus.routingservice.model.Rule;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoutingServiceTests {

    private RoutingService service;
    private List<Rule> rules;

    @BeforeEach
    void setup() {
        rules = new ArrayList<>();
        service = new RoutingService(rules);
    }

    @Test
    void matchRuleSuccessfullyTest() {
        Rule rule = Rule.builder()
                .id(1L)
                .condition("device='iPhone'")
                .destinationURL("/iphone-dashboard")
                .build();
        rules.add(rule);

        Context context = new Context("iPhone", "Safari", "Paris");

        String destinationUrl = service.route(context);

        assertThat(destinationUrl).isEqualTo("/iphone-dashboard");
    }

    @Test
    void returnDefaultWhenNoMatchingRulesTest() {
        Rule rule = Rule.builder()
                .id(1L)
                .condition("device='Android'")
                .destinationURL("/android-dashboard")
                .build();;
        rules.add(rule);

        Context context = new Context("iPhone", "Safari", "Paris");

        String destinationUrl = service.route(context);

        assertThat(destinationUrl).isEqualTo("/default");
    }

    @Test
    void throwExceptionForInvalidConditionFormatTest() {
        Rule invalidRule = Rule.builder()
                .id(1L)
                .condition("invalid-condition")
                .destinationURL("somewhere")
                .build();
        rules.add(invalidRule);

        Context context = new Context("iPhone", "Safari", "Paris");

        String destinationUrl = service.route(context);

        assertThat(destinationUrl).isEqualTo("/default");
    }

    @Test
    void ignoreUnknownFieldInConditionTest() {
        Rule unknownFieldRule = Rule.builder()
                .id(1L)
                .condition("unknown-field='value'")
                .destinationURL("/nowhere")
                .build();
        rules.add(unknownFieldRule);

        Context context = new Context("iPhone", "Safari", "Paris");

        String destinationUrl = service.route(context);

        assertThat(destinationUrl).isEqualTo("/default");
    }

    @Test
    void chooseFirstMatchingRuleTest() {
        Rule firstRule = Rule.builder()
                .id(1L)
                .condition("device='iPhone'")
                .destinationURL("/iphone-first")
                .build();
        Rule secondRule = Rule.builder()
                .id(1L)
                .condition("device='iPhone' AND browser='Safari'")
                .destinationURL("/iphone-safari")
                .build();
        rules.add(firstRule);
        rules.add(secondRule);

        Context context = new Context("iPhone", "Safari", "Paris");

        String destinationUrl = service.route(context);

        assertThat(destinationUrl).isEqualTo("/iphone-first");
    }

    @Test
    void complexRuleMatchBothConditionsTest() {
        Rule rule = new Rule(1L, "device='iPhone' AND browser='Safari'", "/iphone-safari-dashboard");
        rules.add(rule);

        Context context = new Context("iPhone", "Safari", "Paris");

        String destinationUrl = service.route(context);

        assertThat(destinationUrl).isEqualTo("/iphone-safari-dashboard");
    }

    @Test
    void failOnInvalidConditionSyntaxTest() {
        Rule rule = new Rule(1L, "device!=iPhone", "/wrong-url");
        rules.add(rule);

        Context context = new Context("iPhone", "Safari", "Paris");

        String destinationUrl = service.route(context);

        assertThat(destinationUrl).isEqualTo("/default");
    }

    @Test
    void oneConditionFailsThenReturnFalseTest() {
        Rule rule = new Rule(1L, "device='iPhone' AND browser='Firefox'", "/iphone-firefox-dashboard");
        rules.add(rule);

        Context context = new Context("iPhone", "Safari", "Paris");

        String destinationUrl = service.route(context);

        assertThat(destinationUrl).isEqualTo("/default");
    }
}