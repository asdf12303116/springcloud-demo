package com.chen.wcloauth2clientdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@Slf4j
public class TestController {
    @GetMapping("/")
    String index() {
        return "index";
    }

    @GetMapping("/hello")
    public String hello(@RegisteredOAuth2AuthorizedClient("wcl") OAuth2AuthorizedClient authorizedClient,
                        @AuthenticationPrincipal OAuth2User oauth2User){
      log.debug("{}",authorizedClient);
        log.debug("{}",oauth2User);
        return "abc";
    }

    @GetMapping("/callback")
    public String callback(@PathParam("code") String code){

         log.info("{}", code);
        return "123";
    }

}
