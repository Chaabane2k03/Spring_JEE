package tn.fst.classroom_crb.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.fst.classroom_crb.entities.CoursClassroom;
import tn.fst.classroom_crb.entities.Niveau;
import tn.fst.classroom_crb.entities.Specialite;
import tn.fst.classroom_crb.services.ICoursClassroomService;

import java.util.List;

/**
 * URI de base du contrôleur: /api/cours-classrooms
 */
// Stéréotype: contrôleur REST (retourne des JSON)
 @RestController

 @RequestMapping("/api/cours-classrooms")
// Génère un constructeur pour les champs finals (Lombok)
 @RequiredArgsConstructor
public class CoursClassroomRestController {

    private final ICoursClassroomService coursClassroomService;

    // Exigence énoncé (II.c): ajouter un cours et lier à la classe.
    
     @PostMapping("/{codeClasse}")
    public CoursClassroom ajouterCoursClassroom(@RequestBody CoursClassroom cc, @PathVariable Integer codeClasse) {
        return coursClassroomService.ajouterCoursClassroom(cc, codeClasse);
    }

    
     @GetMapping
    public List<CoursClassroom> getAllCoursClassrooms() {
        return coursClassroomService.getAllCoursClassrooms();
    }

    
     @GetMapping("/{idCours}")
    public CoursClassroom getCoursClassroomById(@PathVariable Integer idCours) {
        return coursClassroomService.getCoursClassroomById(idCours);
    }

    
     @PutMapping("/{idCours}")
    public CoursClassroom updateCoursClassroom(@PathVariable Integer idCours, @RequestBody CoursClassroom coursClassroom) {
        return coursClassroomService.updateCoursClassroom(idCours, coursClassroom);
    }

    
     @DeleteMapping("/{idCours}")
    public void deleteCoursClassroom(@PathVariable Integer idCours) {
        coursClassroomService.deleteCoursClassroom(idCours);
    }

    // Exigence énoncé (II.f): désaffecter un cours de sa classe.
    
     @PutMapping("/{idCours}/desaffecter")
    public void desaffecterCoursClassroomClasse(@PathVariable Integer idCours) {
        coursClassroomService.desaffecterCoursClassroomClasse(idCours);
    }

    // Exigence énoncé (II.g - bonus): archivage manuel (en plus du scheduler).
    
     @PostMapping("/archiver")
    public void archiverCoursClassrooms() {
        coursClassroomService.archiverCoursClassrooms();
    }

    // Exigence énoncé (II.h): total heures par spécialité et niveau.
    
     @GetMapping("/heures")
    public Integer nbHeuresParSpecEtNiv(@RequestParam Specialite sp, @RequestParam Niveau nv) {
        return coursClassroomService.nbHeuresParSpecEtNiv(sp, nv);
    }
}
