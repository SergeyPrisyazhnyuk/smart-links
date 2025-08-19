package ru.otus.rulemanagementservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.rulemanagementservice.model.RouteUrl;
import ru.otus.rulemanagementservice.service.RuleService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RuleController.class)
class RuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleService ruleService;

    private List<RouteUrl> exampleRules;

    @BeforeEach
    void setup() {
        exampleRules = Arrays.asList(
                new RouteUrl(1L, "device='iPhone'", "/iphone"),
                new RouteUrl(2L, "device='Android'", "/android")
        );
    }

    @Test
    void listRulesReturnsAllRulesTest() throws Exception {
        given(ruleService.listRules()).willReturn(exampleRules);

        mockMvc.perform(get("/rules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].condition").value("device='iPhone'"))
                .andExpect(jsonPath("$.[1].id").value(2L))
                .andExpect(jsonPath("$.[1].condition").value("device='Android'"));
    }

    @Test
    void createNewRuleIsSuccessfulTest() throws Exception {
        RouteUrl newRule = new RouteUrl(null, "device='Windows'", "/windows");
        given(ruleService.createRule(any())).willReturn(new RouteUrl(3L, "device='Windows'", "/windows"));

        mockMvc.perform(post("/rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":null,\"condition\":\"device='Windows'\",\"destinationURL\":\"/windows\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.condition").value("device='Windows'"))
                .andExpect(jsonPath("$.destinationURL").value("/windows"));
    }

    @Test
    void updateExistingRuleWorksAsExpectedTest() throws Exception {
        RouteUrl existingRule = new RouteUrl(1L, "device='iPhone'", "/iphone");
        RouteUrl updatedRule = new RouteUrl(1L, "device='MacBook'", "/macbook");
        given(ruleService.updateRule(eq(1L), any())).willReturn(updatedRule);

        mockMvc.perform(put("/rules/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"condition\":\"device='MacBook'\",\"destinationURL\":\"/macbook\"} ")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.condition").value("device='MacBook'"))
                .andExpect(jsonPath("$.destinationURL").value("/macbook"));
    }

    @Test
    void deleteRuleRemovesItProperlyTest() throws Exception {

        doNothing().when(ruleService).deleteRule(eq(1L));

        mockMvc.perform(delete("/rules/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void returnsErrorOnNonexistentIdDuringUpdateTest() throws Exception {
        given(ruleService.updateRule(eq(999L), any())).willReturn(null);

        mockMvc.perform(put("/rules/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(" {\"id\":999,\"condition\":\"device='Other'\",\"destinationURL\":\"/other\"} ")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void returnsErrorOnNonexistentIdDuringDeletionTest() throws Exception {
        doThrow(new RuntimeException("Rule not found")).when(ruleService).deleteRule(eq(999L));

        mockMvc.perform(delete("/rules/999"))
                .andExpect(status().isNotFound());

    }
}