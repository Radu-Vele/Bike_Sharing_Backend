package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.SequenceCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class SequenceGeneratorService {
    @Autowired
    private MongoOperations mongoOperations;

    public long generateSequence(String sequenceName) {
        SequenceCount sequence = mongoOperations.findAndModify( //retrieve & update (atomic)
                Query.query(Criteria.where("_id").is(sequenceName)),
                new Update().inc("sequence", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true), //handles the creation of the sequence if non-existing
                SequenceCount.class);
        return sequence.getSequence();
    }
}
