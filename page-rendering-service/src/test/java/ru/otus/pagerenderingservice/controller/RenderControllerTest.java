/*
package ru.otus.pagerenderingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.pagerenderingservice.model.Component;
import ru.otus.pagerenderingservice.model.Template;
import ru.otus.pagerenderingservice.service.TemplateRenderer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RenderController.class)
class RenderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TemplateRenderer renderService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Template sampleTemplate;

    @BeforeEach
    void setup() {
        sampleTemplate = Template.builder()
                .id(1)
                .components(Arrays.asList(
                        Component.builder().type("header").content("<h1>Hello World!</h1>").build(),
                        Component.builder().type("paragraph").content("<p>This is a paragraph.</p>").build()
                ))
                .build();
    }

    @Test
    void renderReturnsHtmlForKnownTemplateTest() throws Exception {
        given(renderService.render(sampleTemplate.getId()))
                .willReturn("<html><h1>Hello World!</h1><p>This is a paragraph.</p></html>");

        mockMvc.perform(get("/render/" + sampleTemplate.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("<html><h1>Hello World!</h1><p>This is a paragraph.</p></html>"));
    }

    @Test
    void renderThrowsErrorForUnknownTemplateTest() throws Exception {
        given(renderService.render(-1))
                .willThrow(new IllegalArgumentException("Шаблон не найден."));

        mockMvc.perform(get("/render/-1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Шаблон не найден."));
    }

    // Тест добавления нового шаблона
    @Test
    void addTemplateAddsNewTemplateTest() throws Exception {
        doNothing().when(renderService).addTemplate(any(Template.class));

        mockMvc.perform(post("/templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Template.builder()
                                        .id(2)
                                        .components(Arrays.asList(
                                                Component.builder().type("footer").content("<footer>Footer section</footer>").build()
                                        ))
                                        .build())))
                .andExpect(status().isOk());
    }

    @Test
    void listTemplatesReturnsCollectionOfTemplatesTest() throws Exception {
        Collection<Template> templates = Arrays.asList(sampleTemplate);
        given(renderService.getTemplates()).willReturn(templates);

        mockMvc.perform(get("/templates"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1,'components':[{'type':'header','content':'<h1>Hello World!</h1>'}, {'type':'paragraph', 'content':'<p>This is a paragraph.</p>'}]}]"));
    }
}
*/
