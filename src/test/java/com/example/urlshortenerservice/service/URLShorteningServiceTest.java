package com.example.urlshortenerservice.service;

import com.example.urlshortenerservice.dto.ShortenURLRequest;
import com.example.urlshortenerservice.model.Link;
import com.example.urlshortenerservice.repository.CounterRepository;
import com.example.urlshortenerservice.repository.LinkRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class URLShorteningServiceTest {

    @InjectMocks
    URLShorteningService urlShorteningService;
    @Mock
    LinkRepository linkRepository;
    @Mock
    Base62Encoder base62Encoder;
    @Mock
    CounterRepository counterRepository;

    @Test
    public void WhenFetchLongUrl_ReturnLongUrl() {
        when(base62Encoder.decode("015ftgG")).thenReturn(1000000000L);
        when(linkRepository.findById(1000000000L)).thenReturn(Optional.of(getLink()));
        assertEquals("https://www.baeldung.com/java-merge-sort", urlShorteningService.getLongURL("015ftgG"));
        verify(base62Encoder).decode("015ftgG");
        verify(linkRepository).findById(1000000000L);
    }

    @Test
    public void WhenInvalidLongURLIsSet_ThenThrowException() {
        InvalidLongUrlException thrown = assertThrows(InvalidLongUrlException.class, () -> {
            urlShorteningService.validateIncomingLongURL("mmmm");
        });
        assertEquals("Invalid long URL", thrown.getMessage());
    }

    @Test
    public void WhenValidLongURLIsSet_ThenDoNotThrowException() {
        assertDoesNotThrow(() -> {
            urlShorteningService.validateIncomingLongURL("https://www.baeldung.com/java-merge-sort");
        });
    }

    @Test
    public void WhenValidLongURLIsSet_ThenCreateShortUrl() {
        when(linkRepository.findByLongUrl("https://www.baeldung.com/java-merge-sort")).thenReturn(null);
        when(linkRepository.insert(any(Link.class))).thenReturn(getLink());
        when(counterRepository.generateSequence(Link.SEQUENCE_NAME)).thenReturn(1000000000L);
        when(base62Encoder.encode(1000000000L)).thenReturn("015ftgG");
        urlShorteningService.shortenUrl(getShortenURLRequest());
        verify(linkRepository).findByLongUrl("https://www.baeldung.com/java-merge-sort");
        verify(linkRepository).insert(any(Link.class));
    }

    private Link getLink() {
        Link link = Link.builder().id(1000000000L).longUrl("https://www.baeldung.com/java-merge-sort").build();
        return link;
    }

    private ShortenURLRequest getShortenURLRequest() {
        return ShortenURLRequest.builder().longUrl("https://www.baeldung.com/java-merge-sort").build();
    }
}