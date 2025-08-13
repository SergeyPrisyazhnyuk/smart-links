package ru.otus.routingservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.routingservice.model.Context;
import ru.otus.routingservice.service.RoutingService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RouterController.class)
class RouterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoutingService routingService;

    @BeforeEach
    void setUp() {
        given(routingService.route(any(Context.class))).willAnswer(invocationOnMock -> {
            Context context = invocationOnMock.getArgument(0);
            return "Routing result for " + context.getDevice() +
                    ", " + context.getBrowser() +
                    ", " + context.getRegion();
        });
    }

    @Test
    void handleValidParametersAndReturnCorrectResponseTest() throws Exception {
        mockMvc.perform(
                        get("/route/context")
                                .param("device", "iPhone")
                                .param("browser", "Safari")
                                .param("region", "Paris")
                )
                .andDo(print()) // Выводит результат в консоль
                .andExpect(status().isOk())
                .andExpect(content().string("Routing result for iPhone, Safari, Paris"));
    }

    @Test
    void handlePartialParametersTest() throws Exception {
        mockMvc.perform(
                        get("/route/context")
                                .param("device", "Linux")
                ) // Передаем только один параметр
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Routing result for Linux, Chrome, Moscow"));
    }

    @Test
    void useDefaultValuesForMissingParametersTest() throws Exception {
        mockMvc.perform(
                        get("/route/context")
                ) // Без передачи параметров
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Routing result for Android, Chrome, Moscow"));
    }

    @Test
    void useDefaultValuesForMissingAndEmptyParametersTest() throws Exception {
        mockMvc.perform(
                        get("/route/context")
                                .param("device", "")
                )
                .andDo(print())
                .andExpect(content().string("Routing result for Android, Chrome, Moscow"));
    }
}