package com.chen.wcloauth2clientdemo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Controller
@RequestMapping(path = {"/webclient", "/public/webclient"})
public class OAuth2WebClientController {
    private final WebClient webClient;

    public OAuth2WebClientController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/explicit")
    String explicit(Model model) {
        String body = this.webClient
                .get()
                .attributes(clientRegistrationId("wcl"))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        model.addAttribute("body", body);
        return "response";
    }

    @GetMapping("/implicit")
    String implicit(Model model) {
        String body = this.webClient
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .block();
        model.addAttribute("body", body);
        return "response";
    }
}
