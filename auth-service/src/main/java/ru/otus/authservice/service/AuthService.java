package ru.otus.authservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.otus.authservice.model.User;
import ru.otus.authservice.repository.UserRepository;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Builder
@Slf4j
public class AuthService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpServletRequest httpServletRequest;

    private static final Pattern BROWSER_PATTERN = Pattern.compile("(Chrome|Firefox|Safari|Edge)");
    private static final Pattern DEVICE_PATTERN = Pattern.compile("(Mobile|Tablet|Desktop)");

    public boolean authenticate(String login, String password) {
        User dbUser = userRepository.findByUsername(login);
/*
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        StringBuilder result = new StringBuilder();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = httpServletRequest.getHeader(headerName);
            result.append(headerName).append(": ").append(headerValue).append("\n");
        }
        log.info(result.toString());

*/
        if (dbUser != null && dbUser.getPassword().equals(password)) {

            String userAgent = httpServletRequest.getHeader("User-Agent");
            String acceptLanguage = httpServletRequest.getHeader("Accept-Language");

            log.info("acceptLanguage : " + acceptLanguage);

            Map<String, Object> routingData = new HashMap<>();
            routingData.put("device", parseDeviceFromUserAgent(userAgent));
            routingData.put("browser", parseBrowserFromUserAgent(userAgent));
            routingData.put("region", extractRegionFromAcceptLanguage(acceptLanguage));

            System.out.println(routingData);

            log.info(routingData.toString());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(routingData, headers);

            try {
                URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8082/route/context")
                        .queryParam("device", routingData.get("device"))
                        .queryParam("browser", routingData.get("browser"))
                        .queryParam("region", routingData.get("region"))
                        .build()
                        .encode()
                        .toUri();

                log.info("URI : " + uri);

               restTemplate.getForObject(uri, String.class);

//                restTemplate.postForObject("http://localhost:8082/route/context",
//                        request,
//                        String.class,
//                        routingData);

                return true;

            } catch (Exception e) {
                log.error("Ошибка связи с Router Controller:", e);
                return false;
            }
        }
        return false;
    }

    private String parseBrowserFromUserAgent(String userAgent) {

        if (userAgent.contains("YaBrowser")) {
            return "YaBrowser";
        }

        Pattern pattern = Pattern.compile("(?i)(chrome|firefox|edge|safari)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userAgent);
        if (matcher.find()) {
            return matcher.group(1).toUpperCase();
        }
        return "UNKNOWN";
    }

    private String parseDeviceFromUserAgent(String userAgent) {
        if (userAgent.contains("Mobile")) {
            return "MOBILE";
        } else if (userAgent.contains("Tablet") || userAgent.contains("iPad")) {
            return "TABLET";
        } else {
            return "DESKTOP";
        }
    }

    private String extractRegionFromAcceptLanguage(String acceptLanguage) {
        if (acceptLanguage == null || acceptLanguage.isEmpty()) {
            return "UNKNOWN";
        }
        String firstLang = acceptLanguage.split(",")[0];
        String[] parts = firstLang.split("-");
        if (parts.length >= 2) {
            return parts[1].toUpperCase();
        } else {
            return parts[0].toUpperCase();
        }
    }

    public User addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            log.info("User with name : " + user.getUsername() + " - already exists");
            return null;
        } else {
            return userRepository.save(user);
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

}