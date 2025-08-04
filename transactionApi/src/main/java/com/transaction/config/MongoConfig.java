package com.transaction.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig implements CommandLineRunner {

    @Autowired
    private MongoClient mongoClient;

    @Override
    public void run(String... args) throws Exception {
        MongoDatabase database = mongoClient.getDatabase("changeapp_transactions_dev");
        
        // Criar índice único no transactionId
        IndexOptions uniqueIndexOptions = new IndexOptions().unique(true);
        database.getCollection("transactions").createIndex(
            new Document("transaction_id", 1),
            uniqueIndexOptions
        );
        
        System.out.println("Índice único do MongoDB criado com sucesso!");
    }
} 