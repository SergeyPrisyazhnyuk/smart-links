package ru.otus.routingservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.otus.routingservice.model.Context;
import ru.otus.routingservice.service.RoutingService;

@RestController
@RequestMapping("/route")
@RequiredArgsConstructor
@Slf4j
public class RouterController {

    @Autowired
    private final RoutingService routingService;

    @GetMapping("/context")
    public String route(@RequestParam(value = "device") String device,
                        @RequestParam(value = "browser") String browser,
                        @RequestParam(value = "region") String region) {

        log.info("Running request with : device= {}, browser = {}, region = {}  ", device, browser, region);
        Context context = Context.builder()
                .device(device)
                .browser(browser)
                .region(region)
                .build();

        return routingService.route(context);
    }
}
