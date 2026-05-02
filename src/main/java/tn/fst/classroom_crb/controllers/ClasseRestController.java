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
import tn.fst.classroom_crb.entities.Classe;
import tn.fst.classroom_crb.services.IClasseService;

import java.util.List;

/**
 * URI de base du contrôleur: /api/classes
 */
@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClasseRestController {

    private final IClasseService classeService;

    // Exigence énoncé (II.b): ajouter les classes.
    @PostMapping
    public Classe ajouterClasse(@RequestBody Classe c) {
        return classeService.ajouterClasse(c);
    }

    @GetMapping
    public List<Classe> getAllClasses() {
        return classeService.getAllClasses();
    }

    @GetMapping("/{codeClasse}")
    public Classe getClasseById(@PathVariable Integer codeClasse) {
        return classeService.getClasseById(codeClasse);
    }

    @PutMapping("/{codeClasse}")
    public Classe updateClasse(@PathVariable Integer codeClasse, @RequestBody Classe classe) {
        return classeService.updateClasse(codeClasse, classe);
    }

    @DeleteMapping("/{codeClasse}")
    public void deleteClasse(@PathVariable Integer codeClasse) {
        classeService.deleteClasse(codeClasse);
    }
}
