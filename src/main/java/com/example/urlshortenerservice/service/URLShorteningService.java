package com.example.urlshortenerservice.service;

import com.example.urlshortenerservice.dto.ShortenURLRequest;
import com.example.urlshortenerservice.dto.ShortenURLResponse;
import com.example.urlshortenerservice.model.Link;
import com.example.urlshortenerservice.repository.CounterRepository;
import com.example.urlshortenerservice.repository.LinkRepository;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class URLShorteningService {

    private final LinkRepository linkRepository;

    private final Base62Encoder base62Encoder;
    private final CounterRepository counterRepository;

    @Value("${application.short.url.server.address}")
    private String shortLinkServerAddress;

    @Autowired
    public URLShorteningService(LinkRepository linkRepository, Base62Encoder base62Encoder, CounterRepository counterRepository){
        this.linkRepository = linkRepository;
        this.base62Encoder   = base62Encoder;
        this.counterRepository = counterRepository;
    }

    @Retryable(retryFor = {RuntimeException.class},
    maxAttempts = 3)
    public ShortenURLResponse shortenUrl (ShortenURLRequest shortenURLRequest) {
        validateIncomingLongURL(shortenURLRequest.getLongUrl());

        Link linkByLongURL = linkRepository.findByLongUrl(shortenURLRequest.getLongUrl());
        if(linkByLongURL != null){
            throw new DuplicateLongUrlException();
        }

        String longUrl = shortenURLRequest.getLongUrl();
        Link link = Link.builder()
                .id(counterRepository.generateSequence(Link.SEQUENCE_NAME))
                .longUrl(longUrl)
                .createdAt(ZonedDateTime.now(ZoneOffset.UTC))
                .build();
        Link insertedLink = linkRepository.insert(link);
        Long id = insertedLink.getId();
        String fullShortURL = shortLinkServerAddress + base62Encoder.encode(id);

        ShortenURLResponse shortenURLResponse = ShortenURLResponse.builder()
                .shortUrl(fullShortURL).longUrl(longUrl).createdAt(insertedLink.getCreatedAt())
                .build();

        return shortenURLResponse;
    }

    //Fetch long url
    public String getLongURL(String shortUrl){

        Long id = base62Encoder.decode(shortUrl);
        Optional<Link> link = linkRepository.findById(id);

        return link.get().getLongUrl();
    }

    public List<Link> getAllUrls (){
        return linkRepository.findAll();

    }

    // Validate the long url
    public boolean validateIncomingLongURL(String longUrl){
        final UrlValidator urlValidator =  new UrlValidator( new String[] { "http", "https" }, UrlValidator.ALLOW_ALL_SCHEMES );
        if (!urlValidator.isValid(longUrl)){
            throw new InvalidLongUrlException("Invalid long URL");
        }
        return true;
    }

}
