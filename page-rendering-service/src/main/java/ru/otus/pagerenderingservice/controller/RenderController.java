package ru.otus.pagerenderingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.otus.pagerenderingservice.model.Template;
import ru.otus.pagerenderingservice.service.RenderService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class RenderController {

    @Autowired
    private final RenderService renderService;

    @GetMapping("/render/{templateId}")
    public String render(@PathVariable Integer templateId) {
        return renderService.render(templateId);
    }

    @PostMapping("/templates")
    public void addTemplate(@RequestBody Template template) {
        renderService.addTemplate(template);
    }

    @GetMapping("/templates")
    public Collection<Template> listTemplates() {
        return renderService.getTemplates();
    }

}