package com.stupzz.association.services;

import com.stupzz.association.exceptions.NotFoundException;
import com.stupzz.association.models.Team;
import com.stupzz.association.models.Tournois;
import com.stupzz.association.repositories.TeamRepository;
import com.stupzz.association.repositories.TournoisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;

@Service
public class TournoisService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TournoisRepository tournoisRepository;

    public Team addEmptyTeam(String tournois_id){
        Tournois tournois = tournoisRepository.findById(tournois_id).orElseThrow(NotFoundException::new);
        if (tournois.getTeams() == null) {
            tournois.setTeams(new ArrayList<>());
        }

        Team team = new Team();
        team.setTeamName("");
        team.setJoueurs(new ArrayList<>());
        team = teamRepository.save(team);

        tournois.getTeams().add(team);
        tournois = tournoisRepository.save(tournois);
        return team;
    }

}
