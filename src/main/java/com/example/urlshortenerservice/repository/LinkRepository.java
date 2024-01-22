package com.example.urlshortenerservice.repository;

import com.example.urlshortenerservice.model.Link;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends MongoRepository<Link, Long> {

}
