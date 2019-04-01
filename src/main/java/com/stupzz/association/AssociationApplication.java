package com.stupzz.association;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AssociationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssociationApplication.class, args);

    }

}
