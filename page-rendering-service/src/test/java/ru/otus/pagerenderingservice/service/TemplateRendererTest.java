package ru.otus.pagerenderingservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.pagerenderingservice.model.PageInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TemplateRendererTest {

    private TemplateRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new TemplateRenderer();
    }

    @Test
    void testRenderNormalScenario() {
        PageInfo info = PageInfo.builder()
                .device("Desktop")
                .browser("Chrome")
                .region("RU")
                .build();

        String actualOutput = renderer.render(info);
        String expectedOutput =
                "<html lang=\"RU\">\n" +
                        " <head>\n" +
                        " <title>Desktop Page</title>\n" +
                        " </head>\n" +
                        " <body>\n" +
                        " <h1>You are using Chrome on Desktop!</h1>\n" +
                        " <p>Your region is: RU.</p>\n" +
                        " </body>\n" +
                        "</html>";

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testRenderWithNullValues() {
        PageInfo info = PageInfo.builder()
                .device(null)
                .browser("Safari")
                .region("US")
                .build();

        String actualOutput = renderer.render(info);
        String expectedOutput =
                "<html lang=\"US\">\n" +
                        " <head>\n" +
                        " <title>null Page</title>\n" +
                        " </head>\n" +
                        " <body>\n" +
                        " <h1>You are using Safari on null!</h1>\n" +
                        " <p>Your region is: US.</p>\n" +
                        " </body>\n" +
                        "</html>";

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testRenderWithEmptyStrings() {
        PageInfo info = PageInfo.builder()
                .device("")
                .browser("")
                .region("")
                .build();

        String actualOutput = renderer.render(info);
        String expectedOutput =
                "<html lang=\"\">\n" +
                        " <head>\n" +
                        " <title> Page</title>\n" +
                        " </head>\n" +
                        " <body>\n" +
                        " <h1>You are using  on !</h1>\n" +
                        " <p>Your region is: .</p>\n" +
                        " </body>\n" +
                        "</html>";

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testRenderWithMaximumLengthStrings() {
        String maxStr = "x".repeat(255);
        PageInfo info = PageInfo.builder()
                .device(maxStr)
                .browser(maxStr)
                .region(maxStr)
                .build();

        String actualOutput = renderer.render(info);
        String expectedOutput =
                "<html lang=\"" + maxStr + "\">\n" +
                        " <head>\n" +
                        " <title>" + maxStr + " Page</title>\n" +
                        " </head>\n" +
                        " <body>\n" +
                        " <h1>You are using " + maxStr + " on " + maxStr + "!</h1>\n" +
                        " <p>Your region is: " + maxStr + ".</p>\n" +
                        " </body>\n" +
                        "</html>";

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testRenderMinimalDocument() {
        PageInfo info = PageInfo.builder()
                .device("D")
                .browser("B")
                .region("R")
                .build();

        String actualOutput = renderer.render(info);
        String expectedOutput =
                "<html lang=\"R\">\n" +
                        " <head>\n" +
                        " <title>D Page</title>\n" +
                        " </head>\n" +
                        " <body>\n" +
                        " <h1>You are using B on D!</h1>\n" +
                        " <p>Your region is: R.</p>\n" +
                        " </body>\n" +
                        "</html>";

        assertEquals(expectedOutput, actualOutput);
    }
}
