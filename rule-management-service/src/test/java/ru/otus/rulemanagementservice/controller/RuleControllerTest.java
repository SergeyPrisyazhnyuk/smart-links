package ru.otus.rulemanagementservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.otus.common.model.MatchingResult;
import ru.otus.rulemanagementservice.model.RouteUrl;
import ru.otus.rulemanagementservice.service.RuleService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RuleControllerTest {

    @Mock
    private RuleService ruleService;

    @InjectMocks
    private RuleController ruleController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ruleController).build();
    }


    @Test
    void testMatchRoutes_Success() throws Exception {
        MatchingResult matchingResult = new MatchingResult(Arrays.asList("/rule-matched"));
        when(ruleService.match(eq("android"), eq("chrome"), eq("RU")))
                .thenReturn(matchingResult);

        mockMvc.perform(get("/rules/match")
                        .param("device", "android")
                        .param("browser", "chrome")
                        .param("region", "RU"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['urls'][0]", containsString("/rule-matched")));
    }

    @Test
    void testListRules() throws Exception {
        List<RouteUrl> routes = Arrays.asList(new RouteUrl(1L, "/url1"), new RouteUrl(2L, "/url2"));
        when(ruleService.listRules()).thenReturn(routes);

        mockMvc.perform(get("/rules"))
                .andDo(result -> System.out.println("Response body: " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1]['destinationURL']", containsString("/url2")));
    }

    @Test
    void testCreateRule() throws Exception {
        RouteUrl newRule = new RouteUrl(1L, "/new-rule");
        when(ruleService.createRule(any(RouteUrl.class)))
                .thenReturn(newRule);

        mockMvc.perform(post("/rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"url\":\"/new-rule\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destinationURL", containsString("/new-rule")));
    }

    @Test
    void testUpdateRule_Success() throws Exception {
        RouteUrl updatedRule = new RouteUrl(1L, "/updated-rule");
        when(ruleService.updateRule(eq(1L), any(RouteUrl.class)))
                .thenReturn(updatedRule);

        mockMvc.perform(put("/rules/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"url\":\"/updated-rule\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destinationURL", containsString("/updated-rule")));
    }

    @Test
    void testUpdateRule_NotFound() throws Exception {
        when(ruleService.updateRule(eq(2L), any(RouteUrl.class)))
                .thenReturn(null);

        mockMvc.perform(put("/rules/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":2,\"url\":\"/nonexistent-rule\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteRule_Success() throws Exception {
        mockMvc.perform(delete("/rules/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteRule_NotFound() throws Exception {
        RuntimeException ex = new RuntimeException("Rule not found");
        doThrow(ex).when(ruleService).deleteRule(eq(2L));

        mockMvc.perform(delete("/rules/2"))
                .andExpect(status().isNotFound());
    }
}