package com.stupzz.association.controllers;

import com.stupzz.association.exceptions.NotFoundException;
import com.stupzz.association.models.Joueur;
import com.stupzz.association.models.Team;
import com.stupzz.association.models.Tournois;
import com.stupzz.association.repositories.JoueurRepository;
import com.stupzz.association.repositories.TeamRepository;
import com.stupzz.association.repositories.TournoisRepository;
import com.stupzz.association.services.TournoisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.ArrayList;

@Controller
public class TeamController {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TournoisRepository tournoisRepository;
    @Autowired
    private JoueurRepository joueurRepository;
    @Autowired
    private TournoisService tournoisService;

    @GetMapping("/new-team")
    public String teams(Model model, @RequestParam String tournois_id) {
        Tournois tournois = tournoisRepository.findById(tournois_id).orElseThrow(NotFoundException::new);
        Team team = new Team();
        team.setJoueurs(new ArrayList<>());

        for (int i = 0; i < tournois.getNbJoueur(); ++i) {
            team.getJoueurs().add(new Joueur());
        }

        model.addAttribute("team", team);
        model.addAttribute("tournoi", tournois);

        return "edit-team";

        //Team team = tournoisService.addEmptyTeam(tournois_id);
        //return "redirect:/edit-team?team_id=" + team.getId() + "&tournois_id="+ tournois_id;
    }

    @PostMapping("/new-team")
    public String newTeam(@Valid @ModelAttribute Team form, BindingResult bindingResult, @ModelAttribute("tournoi") Tournois tournois, @RequestParam String tournois_id) {
        if (tournois == null || tournois.getId() == null || tournois.getTeams() == null) {
            tournois = tournoisRepository.findById(tournois_id).get();
        }
        if (bindingResult.hasErrors()) {
            return "edit-team";
        }

        for(Joueur joueur : form.getJoueurs()){
            joueurRepository.save(joueur);
        }
        form = teamRepository.save(form);

        @Valid Team finalForm = form;
        System.out.println("debug ==================");
        System.out.println(tournois.getTeams());
        if (tournois.getTeams().stream().noneMatch(team -> {
            System.out.println(team);
            return team.getId().equals(finalForm.getId());
        })) {
            tournois.getTeams().add(form);
        }
        tournoisRepository.save(tournois);

        return "redirect:/team?team_id=" +  form.getId() + "&tournois_id=" + tournois_id;
    }

    @GetMapping("/edit-team")
    public String editTeam(Model model, @RequestParam String team_id, @RequestParam String tournois_id) {
        Tournois tournois = tournoisRepository.findById(tournois_id).get();
        Team team = teamRepository.findById(team_id).get();

        if (team.getJoueurs() == null) {
            team.setJoueurs(new ArrayList<>());
        }
        if (team.getJoueurs().size() < tournois.getNbJoueur()) {
            for (int i = team.getJoueurs().size(); i < tournois.getNbJoueur(); ++i) {
                team.getJoueurs().add(new Joueur());
            }
        }

        model.addAttribute("team", team);
        model.addAttribute("tournoi", tournois);
        return "edit-team";
    }

    @GetMapping("/team")
    public String showTeam(Model model, @RequestParam String team_id, @RequestParam String tournois_id) {
        model.addAttribute("team", teamRepository.findById(team_id).get());
        model.addAttribute("tournois_id", tournois_id);
        return "team";
    }
    @PostMapping("/team")
    public String addTeam(Model model, @RequestParam String team_id) {
        model.addAttribute("team", teamRepository.findById(team_id).get());

        return "team";
    }

}