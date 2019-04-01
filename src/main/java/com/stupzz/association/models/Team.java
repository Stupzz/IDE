package com.stupzz.association.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Team {
    @Indexed(unique = true)
    @NotBlank
    private String teamName;
    @DBRef @Valid
    private List<Joueur> joueurs;
    @Id
    private String id;
    private String imageName;

}
