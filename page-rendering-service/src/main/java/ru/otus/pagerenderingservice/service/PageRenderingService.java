package ru.otus.pagerenderingservice.service;

import org.springframework.stereotype.Service;
import ru.otus.pagerenderingservice.model.PageInfo;


@Service
public class PageRenderingService {

    public String render(PageInfo info) {
        return String.format("<html lang=\"%s\">\n" +
                        " <head>\n" +
                        " <title>%s Page</title>\n" +
                        " </head>\n" +
                        " <body>\n" +
                        " <h1>You are using %s on %s!</h1>\n" +
                        " <p>Your region is: %s.</p>\n" +
                        " </body>\n" +
                        "</html>",
                info.getRegion(),
                info.getDevice(),
                info.getBrowser(),
                info.getDevice(),
                info.getRegion());
    }
}