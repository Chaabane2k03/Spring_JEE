package tn.fst.classroom_crb.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.fst.classroom_crb.entities.Niveau;
import tn.fst.classroom_crb.entities.Utilisateur;
import tn.fst.classroom_crb.services.IUtilisateurService;

import java.util.List;

/**
 * URI de base du contrôleur: /api/utilisateurs
 */
// Stéréotype: contrôleur REST (retourne des JSON)
@RestController

@RequestMapping("/api/utilisateurs")
// Génère un constructeur pour les champs finals (Lombok)
@RequiredArgsConstructor
public class UtilisateurRestController {

    private final IUtilisateurService utilisateurService;

    // Exigence énoncé (II.a): ajouter les utilisateurs.

    @PostMapping
    public Utilisateur ajouterUtilisateur(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.ajouterUtilisateur(utilisateur);
    }

    @GetMapping
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    @GetMapping("/{idUtilisateur}")
    public Utilisateur getUtilisateurById(@PathVariable Integer idUtilisateur) {
        return utilisateurService.getUtilisateurById(idUtilisateur);
    }

    @PutMapping("/{idUtilisateur}")
    public Utilisateur updateUtilisateur(@PathVariable Integer idUtilisateur, @RequestBody Utilisateur utilisateur) {
        return utilisateurService.updateUtilisateur(idUtilisateur, utilisateur);
    }

    @DeleteMapping("/{idUtilisateur}")
    public void deleteUtilisateur(@PathVariable Integer idUtilisateur) {
        utilisateurService.deleteUtilisateur(idUtilisateur);
    }

    // Exigence énoncé (II.d): affecter utilisateur -> classe.

    @PutMapping("/{idUtilisateur}/classes/{codeClasse}")
    public void affecterUtilisateurClasse(@PathVariable Integer idUtilisateur, @PathVariable Integer codeClasse) {
        utilisateurService.affecterUtilisateurClasse(idUtilisateur, codeClasse);
    }

    // Exigence énoncé (II.e): nombre d'utilisateurs pour un niveau.

    @GetMapping("/niveau/{nv}/count")
    public Integer nbUtilisateursParNiveau(@PathVariable Niveau nv) {
        return utilisateurService.nbUtilisateursParNiveau(nv);
    }
}
