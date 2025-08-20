package ru.otus.pagerenderingservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.otus.pagerenderingservice.model.PageInfo;
import ru.otus.pagerenderingservice.service.PageRenderingService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RenderControllerTest {

    @Mock
    private PageRenderingService pageRenderingService;

    @InjectMocks
    private RenderController controller;

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testRenderPagePositive() throws Exception {
        when(pageRenderingService.render(any(PageInfo.class))).thenReturn("<html><head></head><body>Hello World!</body></html>");

        mvc.perform(get("/desktop/chrome/ru"))
                .andExpect(status().isOk())
                .andExpect(content().string("<html><head></head><body>Hello World!</body></html>"));
    }

    @Test
    void testRenderPageNegative() throws Exception {
        String invalidDevice = "";
        String invalidBrowser = "";
        String invalidRegion = "";

        mvc.perform(get("/" + invalidDevice + "/" + invalidBrowser + "/" + invalidRegion))
                .andExpect(status().isNotFound());
    }

}