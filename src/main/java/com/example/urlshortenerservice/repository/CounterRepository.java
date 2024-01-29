package com.example.urlshortenerservice.repository;

import com.example.urlshortenerservice.model.DatabaseSequence;
import com.example.urlshortenerservice.service.Base62Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CounterRepository {

    private final MongoOperations mongoOperations;

    @Autowired
    public CounterRepository(MongoOperations mongoOperations){
        this.mongoOperations = mongoOperations;
    }

    //Fetch next sequence for ID
    public long generateSequence(String seqName) {
        DatabaseSequence counter = mongoOperations.findAndModify(Query.query(Criteria.where("_id").is(seqName)),
                new Update().inc("seq",1), FindAndModifyOptions.options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return counter.getSeq();
    }
}
