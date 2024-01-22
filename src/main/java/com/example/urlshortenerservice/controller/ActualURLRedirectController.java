package com.example.urlshortenerservice.controller;

import com.example.urlshortenerservice.service.URLShorteningService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class ActualURLRedirectController {

    private final URLShorteningService urlShorteningService;

    @Autowired
    public ActualURLRedirectController(URLShorteningService urlShorteningService){
        this.urlShorteningService = urlShorteningService;
    }


    @GetMapping(value="/{shortUrl}")
    @Operation(summary = "${usu.getlongurl}")
    public RedirectView redirectWithUsingRedirectView(@PathVariable("shortUrl") String shortUrl) {
        String longURL = urlShorteningService.getLongURL(shortUrl);
        return new RedirectView(longURL);
    }

}
