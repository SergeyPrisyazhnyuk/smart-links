package ru.otus.routingservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.otus.routingservice.model.Context;
import ru.otus.routingservice.service.RoutingService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RouterControllerTest {

    @Mock
    private RoutingService routingService;

    @InjectMocks
    private RouterController routerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(routerController).build();
    }

    @Test
    void testRouteSuccess() throws Exception {
        String expectedResult = "/homepage";
        when(routingService.route(any(Context.class))).thenReturn(expectedResult);

        mockMvc.perform(get("/route/context")
                        .param("device", "desktop")
                        .param("browser", "chrome")
                        .param("region", "RU"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResult));
    }

    @Test
    void testRouteBadInput() throws Exception {
        String expectedResult = "/error-page";
        when(routingService.route(any(Context.class))).thenReturn(expectedResult);

        mockMvc.perform(get("/route/context")
                        .param("device", "")
                        .param("browser", "invalid-browser")
                        .param("region", ""))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResult));
    }

    @Test
    void testRouteEmptyParameters() throws Exception {
        String expectedResult = "/default-page";
        when(routingService.route(any(Context.class))).thenReturn(expectedResult);

        mockMvc.perform(get("/route/context")
                        .param("device", "")
                        .param("browser", "")
                        .param("region", ""))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResult));
    }

    @Test
    void testRouteLargeStrings() throws Exception {
        String largeString = "X".repeat(255);

        String expectedResult = "/large-string-page";
        when(routingService.route(any(Context.class))).thenReturn(expectedResult);

        mockMvc.perform(get("/route/context")
                        .param("device", largeString)
                        .param("browser", largeString)
                        .param("region", largeString))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResult));
    }

    @Test
    void testRouteSpecialChars() throws Exception {
        String specialChars = "@#!$$%%^^&&***";

        String expectedResult = "/special-chars-page";
        when(routingService.route(any(Context.class))).thenReturn(expectedResult);

        mockMvc.perform(get("/route/context")
                        .param("device", specialChars)
                        .param("browser", specialChars)
                        .param("region", specialChars))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResult));
    }
}