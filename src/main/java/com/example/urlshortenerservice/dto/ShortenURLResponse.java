package com.example.urlshortenerservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.ZonedDateTime;


@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = ShortenURLResponse.Builder.class)
public class ShortenURLResponse implements Serializable {

    private String shortUrl;
    private String longUrl;
    private ZonedDateTime createdAt;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder
    {
    }
}
