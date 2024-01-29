package com.example.urlshortenerservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = ShortenURLRequest.Builder.class)
public class ShortenURLRequest implements Serializable {
    @NotBlank(message = "LongUrl is mandatory")
    @NotNull(message = "LongUrl is mandatory")
    private String longUrl;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder
    {
    }

}
