package com.example.urlshortenerservice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShortenURLTestResponse {
    private String shortUrl;
    private String longUrl;

    @JsonIgnore
    private ZonedDateTime createdAt;
}
