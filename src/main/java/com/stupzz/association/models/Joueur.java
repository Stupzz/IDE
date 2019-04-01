package com.stupzz.association.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Joueur {
    @NotBlank
    private String name;
    @NotBlank
    private String mail;
    @NotBlank
    private String pseudo;
    @Id
    private String id;
}
