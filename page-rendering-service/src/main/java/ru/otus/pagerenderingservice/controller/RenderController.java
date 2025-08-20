package ru.otus.pagerenderingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.pagerenderingservice.model.PageInfo;
import ru.otus.pagerenderingservice.service.TemplateRenderer;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class RenderController {

    @Autowired
    private TemplateRenderer templateRenderer;

    @GetMapping("/{device}/{browser}/{region}")
    public String renderPage(
            @PathVariable String device,
            @PathVariable String browser,
            @PathVariable String region) {

        PageInfo pageInfo = PageInfo.builder()
                .device(device)
                .browser(browser)
                .region(region)
                .build();

        return templateRenderer.render(pageInfo);
    }
}
