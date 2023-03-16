package com.backend.se_project_backend.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration{
    @Value("${spring.data.mongodb.uri}")
    private String uri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(uri);
    }

    /**
     * Provides more control over DB operations
     * @return
     */
    public MongoTemplate mongoTemplate() {
            return new MongoTemplate(mongoClient(), databaseName);
    }

    /**
     * used to allow indexed fields in mongodb collection
     */
    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}