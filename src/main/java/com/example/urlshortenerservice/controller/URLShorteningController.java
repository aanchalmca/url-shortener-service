package com.example.urlshortenerservice.controller;

import com.example.urlshortenerservice.dto.ShortenURLRequest;
import com.example.urlshortenerservice.dto.ShortenURLResponse;
import com.example.urlshortenerservice.model.Link;
import com.example.urlshortenerservice.service.DuplicateLongUrlException;
import com.example.urlshortenerservice.service.InvalidLongUrlException;
import com.example.urlshortenerservice.service.URLShorteningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value="/shortenUrl")
@Tag(name="${usu.apis.tag}")
public class URLShorteningController
{
    private final URLShorteningService urlShorteningService;

    @Autowired
    public URLShorteningController(URLShorteningService urlShorteningService){
        this.urlShorteningService = urlShorteningService;
    }

    /**
     * Create the short url for the corresponding longurl provided as input
     * @param shortenURLRequest
     * @return
     */
   @PostMapping(value="/create")
   @Operation(summary = "${usu.create.shortenurl}")
    public ResponseEntity<ShortenURLResponse> shortenURL(@Valid @RequestBody ShortenURLRequest shortenURLRequest){
       return new ResponseEntity<>(urlShorteningService.shortenUrl(shortenURLRequest),
               HttpStatus.OK);
    }

    @GetMapping(value="/findAllUrls")
    public List<Link> getAllURls(){
       return urlShorteningService.getAllUrls();
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Input Long URL is incorrect")
    @ExceptionHandler(InvalidLongUrlException.class)
    public void invalidLongUrl() {
        log.error("Invalid long URL");
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Long URL already exists")
    @ExceptionHandler(DuplicateLongUrlException.class)
    public void duplicateLongUrl() {
        log.error("Long URL already exits");
    }


}
