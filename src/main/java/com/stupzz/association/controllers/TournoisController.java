package com.stupzz.association.controllers;

import com.stupzz.association.models.Tournois;
import com.stupzz.association.repositories.TournoisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
@Controller
public class TournoisController {
    @Autowired
    private TournoisRepository tournoisRepository;

    @GetMapping("/new-tournois")
    public String newTournois(Model model) {
        model.addAttribute("newTournois", new Tournois());
        return "new-tournois";
    }

    @PostMapping("/new-tournois")
    public String newTournoi(@Valid @ModelAttribute("newTournois") Tournois newTournois, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return "new-tournois";
        }

        newTournois.setTeams( new ArrayList<>() );
        newTournois = tournoisRepository.save(newTournois);
        return "redirect:/page-tournois?tournois_id=" + newTournois.getId();
    }

    @GetMapping("/page-tournois")
    public String newTournois(Model model, @RequestParam String tournois_id) {
        model.addAttribute("tournois", tournoisRepository.findById(tournois_id).get());
        System.out.println(tournoisRepository.findById(tournois_id).get());
        return "page-tournois";
    }

    @GetMapping("/")
    public String tournois(Model model) {
        model.addAttribute("tournois", tournoisRepository.findAll());
        return "tournois";
    }
}