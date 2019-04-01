package com.stupzz.association.models;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.*;
import java.text.SimpleDateFormat;
import java.util.List;

@Data
public class Tournois {
    @Positive
    private int nbJoueur;
    @Indexed(unique = true)
    @NotBlank
    private String name;
    @Id
    private String id;
    @DBRef
    private List<Team> teams;
    private String description;
    //private SimpleDateFormat dateTournois;

}
