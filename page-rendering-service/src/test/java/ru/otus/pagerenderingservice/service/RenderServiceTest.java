/*
package ru.otus.pagerenderingservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.pagerenderingservice.model.Component;
import ru.otus.pagerenderingservice.model.Template;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class RenderServiceTest {

    private TemplateRenderer renderService;

    @BeforeEach
    void setup() {
        renderService = new TemplateRenderer();
    }

    @Test
    void renderReturnsHTMLForExistingTemplateTest() {
        Template template = Template.builder()
                .id(1)
                .components(Arrays.asList(
                        Component.builder().type("header").content("<h1>Заголовок страницы</h1>").build(),
                        Component.builder().type("paragraph").content("<p>Основной контент страницы.</p>").build()
                ))
                .build();

        renderService.addTemplate(template);

        String renderedHtml = renderService.render(template.getId());

        String normalizedExpected = normalizeHtml("<html>\n<h1>Заголовок страницы</h1>\n<p>Основной контент страницы.</p></html>");
        String normalizedActual = normalizeHtml(renderedHtml);

        assertEquals(normalizedExpected, normalizedActual);
    }

    @Test
    void renderThrowsExceptionForNonExistentTemplateTest() {
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> renderService.render(999));

        assertEquals("Шаблон не найден.", thrown.getMessage());
    }

    @Test
    void addTemplateStoresTemplateInStorageTest() {
        Template newTemplate = Template.builder()
                .id(2)
                .components(Arrays.asList(
                        Component.builder().type("footer").content("<footer>Подвал сайта</footer>").build()
                ))
                .build();

        renderService.addTemplate(newTemplate);

        Collection<Template> storedTemplates = renderService.getTemplates();
        assertTrue(storedTemplates.contains(newTemplate));
    }

    @Test
    void getTemplatesReturnsCollectionOfStoredTemplatesTest() {
        Template t1 = Template.builder().id(1).components(Arrays.asList(Component.builder().type("header").content("<h1>Header</h1>").build())).build();
        Template t2 = Template.builder().id(2).components(Arrays.asList(Component.builder().type("footer").content("<footer>Footer</footer>").build())).build();

        renderService.addTemplate(t1);
        renderService.addTemplate(t2);

        Collection<Template> allTemplates = renderService.getTemplates();

        assertEquals(2, allTemplates.size());
        assertTrue(allTemplates.contains(t1));
        assertTrue(allTemplates.contains(t2));
    }

    private String normalizeHtml(String html) {
        return html.replaceAll("\\s+", " ").trim();
    }

}*/
