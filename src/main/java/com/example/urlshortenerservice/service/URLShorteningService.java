package com.example.urlshortenerservice.service;

import com.example.urlshortenerservice.dto.ShortenURLRequest;
import com.example.urlshortenerservice.dto.ShortenURLResponse;
import com.example.urlshortenerservice.model.DatabaseSequence;
import com.example.urlshortenerservice.model.Link;
import com.example.urlshortenerservice.repository.LinkRepository;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class URLShorteningService {

    private final LinkRepository linkRepository;
    private final MongoOperations mongoOperations;

    @Value("${application.short.url.server.address}")
    private String shortLinkServerAddress;

    @Autowired
    public URLShorteningService(LinkRepository linkRepository, MongoOperations mongoOperations){
        this.linkRepository = linkRepository;
        this.mongoOperations = mongoOperations;
    }

//    TODO: @Retryable
    public ShortenURLResponse shortenUrl (ShortenURLRequest shortenURLRequest) {
        if (!validateIncomingLongURL(shortenURLRequest.getLongUrl())){
            throw new InvalidLongUrlException();
        }

        String longUrl = shortenURLRequest.getLongUrl();
        Link link = Link.builder()
                .id(generateSequence(Link.SEQUENCE_NAME))
                .longUrl(longUrl)
                .createdAt(ZonedDateTime.now(ZoneOffset.UTC))
                .build();
        Link insertedLink = linkRepository.insert(link);
        Long id = insertedLink.getId();
        String fullShortURL = shortLinkServerAddress + Base62Encoder.encode(id);

        ShortenURLResponse shortenURLResponse = ShortenURLResponse.builder()
                .shortUrl(fullShortURL).longUrl(longUrl).createdAt(insertedLink.getCreatedAt())
                .build();

        return shortenURLResponse;
    }

    public long generateSequence(String seqName) {
        DatabaseSequence counter = mongoOperations.findAndModify(Query.query(Criteria.where("_id").is(seqName)),
                new Update().inc("seq",1), FindAndModifyOptions.options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return counter.getSeq();
    }

    /*public List<URLShorteningEntity> getAllURLs(){
        return linkRepository.getAllURLs();
    }*/

    public String getLongURL(String shortUrl){
//        if (shortUrl.split("\\/").length != 2)
//            return "Invalid short url";

//        String encodedString = shortUrl.split("\\/")[1];
        Long id = Base62Encoder.decode(shortUrl);
        Optional<Link> link = linkRepository.findById(id);

        return link.get().getLongUrl();
    }

    // Validate the long url
    private boolean validateIncomingLongURL(String longUrl){
        final UrlValidator urlValidator =  new UrlValidator( new String[] { "http", "https" }, UrlValidator.ALLOW_ALL_SCHEMES );
        return urlValidator.isValid(longUrl);
    }

}
