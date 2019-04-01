package com.stupzz.association.repositories;

import com.stupzz.association.models.Tournois;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TournoisRepository extends MongoRepository<Tournois, String> {
}
