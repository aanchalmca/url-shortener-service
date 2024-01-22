package com.example.urlshortenerservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = ShortenURLRequest.Builder.class)
public class ShortenURLRequest implements Serializable {
    private String longUrl;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder
    {
    }

}
