package com.stupzz.association.controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

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
    public String newTeam(@Valid @ModelAttribute Team form, BindingResult bindingResult, @ModelAttribute("tournoi") Tournois tournois, @RequestParam String tournois_id, Model model) {
        if (tournois == null || tournois.getId() == null || tournois.getTeams() == null) {
            tournois = tournoisRepository.findById(tournois_id).get();
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("team", form);
            model.addAttribute("tournoi", tournois);
            return "edit-team";
        }

        for (Joueur joueur : form.getJoueurs()) {
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

        return "redirect:/team?team_id=" + form.getId() + "&tournois_id=" + tournois_id;
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

    @GetMapping("/pdf")
    public String createPdf(@RequestParam String team_id, @RequestParam String tournois_id) {
        Team team = teamRepository.findById(team_id).get();
        Tournois tournois = tournoisRepository.findById(tournois_id).get();
        try {

            Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
                    Font.BOLD);
            Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.NORMAL, BaseColor.RED);
            Font teamFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,
                    Font.NORMAL);
            Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD);
            Document document = new Document();
            //String pathName = "D:\\Programmation\\IDE\\src\\main\\java\\com\\stupzz\\association\\pdfs\\".concat(team.getTeamName()).concat(".pdf");

            String pathName = new File(".").getCanonicalPath().concat(team.getTeamName()).concat(".pdf");
            System.out.println("File saved at:" + pathName);
            PdfWriter.getInstance(document, new FileOutputStream(pathName));
            document.open();
            document.addTitle("Inscription au tournoi " + tournois.getName());
            document.addKeywords("PDF, inscription");
            document.addAuthor("Mathieu");
            document.addCreator("Mathieu");

            Paragraph preface = new Paragraph();
            // We add one empty line
            addEmptyLine(preface, 1);
            // Lets write a big header
            preface.add(new Paragraph("Inscription au tournoi " + tournois.getName() + ": " + team.getTeamName(), catFont));

            addEmptyLine(preface, 1);
            // Will create: Report generated by: _name, _date
            preface.add(new Paragraph(
                    "Report generated by: " + team.getTeamName() + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    smallBold));
            addEmptyLine(preface, 2);
            preface.add(new Paragraph(
                    "Ce document atteste de votre inscription au tournoi.",
                    redFont));

            addEmptyLine(preface, 4);

            document.add(preface);

            Paragraph teamName = new Paragraph("Team: " + team.getTeamName()+ ". Composez de:", teamFont);
            Paragraph description = new Paragraph("Description du tournois: " + tournois.getDescription()+".", smallBold);
            addEmptyLine(description, 5);
            document.add(description);
            addEmptyLine(teamName, 2);
            document.add(teamName);
            PdfPTable table = new PdfPTable(team.getJoueurs().size());

            for (int i = 0; i < team.getJoueurs().size(); ++i) {
                PdfPCell cell = new PdfPCell(new Phrase("Joueur " + (i + 1)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            //table.setHeaderRows(1);

            for (Joueur joueur : team.getJoueurs()) {
                table.addCell("Nom: " + joueur.getName() + " / " +
                        "Pseudo: " + joueur.getPseudo() + " / " +
                        "Mail: " + joueur.getMail());
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/team?team_id=" + team_id + "&tournois_id=" + tournois_id;
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

}