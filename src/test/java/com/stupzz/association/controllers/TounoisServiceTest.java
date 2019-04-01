package com.stupzz.association.controllers;

import com.stupzz.association.exceptions.NotFoundException;
import com.stupzz.association.models.Team;
import com.stupzz.association.models.Tournois;
import com.stupzz.association.repositories.TeamRepository;
import com.stupzz.association.repositories.TournoisRepository;
import com.stupzz.association.services.TournoisService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static org.junit.Assert.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TounoisServiceTest {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TournoisRepository tournoisRepository;
    @Autowired
    private TournoisService tournoisService;
    @Before
    public void cleanDataBase(){
        tournoisRepository.deleteAll();
        teamRepository.deleteAll();
    }

    @Test
    public void addEmptyTeamTournois() {
        Tournois tournois = new Tournois();
        tournois.setName("testttttt");
        tournois.setNbJoueur(1);
        tournoisRepository.save(tournois);

        Team team = tournoisService.addEmptyTeam(tournois.getId());
        assertThat(team.getJoueurs()).hasSize(0);
        assertThat(team.getTeamName()).isEqualTo("");
        assertThat(team.getId()).isNotNull();
    }

    @Test
    public void testNoNameTournament() {
        Tournois tournois = new Tournois();
        tournois.setNbJoueur(1);
        tournoisRepository.save(tournois);
    }

    @Test (expected = ConstraintViolationException.class)
    public void testNonValidTournois() {
        Tournois tournois = new Tournois();
        tournois.setName("testttttt");
        tournois.setNbJoueur(-1);
        tournoisRepository.save(tournois);
    }

    @Test(expected = NotFoundException.class)
    public void addEmptyTeamToNonExistingTournois() {
        Team team = tournoisService.addEmptyTeam("nonExistingTournois");
        assertThat(team.getJoueurs()).hasSize(0);
        assertThat(team.getTeamName()).isEqualTo("");
        assertThat(team.getId()).isNotNull();
    }

    @Test
    public void newTeam() {
    }

    @Test
    public void editTeam() {
    }

    @Test
    public void showTeam() {
    }

    @Test
    public void addTeam() {
    }
}