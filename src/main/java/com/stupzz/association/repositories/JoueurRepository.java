package com.stupzz.association.repositories;

import com.stupzz.association.models.Joueur;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JoueurRepository extends MongoRepository<Joueur, String> {
}
