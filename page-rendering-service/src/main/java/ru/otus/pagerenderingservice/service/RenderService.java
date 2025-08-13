package ru.otus.pagerenderingservice.service;

import org.springframework.stereotype.Service;
import ru.otus.pagerenderingservice.model.Component;
import ru.otus.pagerenderingservice.model.Template;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RenderService {

    private final Map<Integer, Template> templates = new ConcurrentHashMap<>();

    public String render(Integer templateId) {
        Template template = templates.get(templateId);
        if (template == null) {
            throw new IllegalArgumentException("Шаблон не найден.");
        }
        return "<html>" +
                template.getComponents().stream()
                        .map(Component::getContent)
                        .reduce("", (a, b) -> a + "\n" + b) +
                "</html>";
    }

    public void addTemplate(Template template) {
        templates.put(template.getId(), template);
    }


    public Collection<Template> getTemplates() {
        return templates.values();
    }
}