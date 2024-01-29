package com.example.urlshortenerservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("links")
public class Link {

    @Transient
    public static final String SEQUENCE_NAME = "SHORT_URL_UNIQUE_ID";

    @Id
    private Long id;


    @Indexed
    private String longUrl;

    @CreatedDate
    private ZonedDateTime createdAt;

}
