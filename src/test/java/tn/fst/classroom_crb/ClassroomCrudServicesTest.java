package tn.fst.classroom_crb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.fst.classroom_crb.entities.Classe;
import tn.fst.classroom_crb.entities.CoursClassroom;
import tn.fst.classroom_crb.entities.Niveau;
import tn.fst.classroom_crb.entities.Specialite;
import tn.fst.classroom_crb.entities.Utilisateur;
import tn.fst.classroom_crb.services.IClasseService;
import tn.fst.classroom_crb.services.ICoursClassroomService;
import tn.fst.classroom_crb.services.IUtilisateurService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Charge le contexte Spring pour les tests
 @SpringBootTest
// Active un profil Spring spécifique pour le test/configuration
 @ActiveProfiles("test")
class ClassroomCrudServicesTest {

    // Injection de dépendance par Spring
     @Autowired
    private IUtilisateurService utilisateurService;

    // Injection de dépendance par Spring
     @Autowired
    private IClasseService classeService;

    // Injection de dépendance par Spring
     @Autowired
    private ICoursClassroomService coursClassroomService;

    // Indique une méthode de test (JUnit)
     @Test
    void crudComplet_shouldWorkForAllServices() {
        Classe classe = new Classe();
        classe.setTitre("3INFO1");
        classe.setNiveau(Niveau.TROISIEME);
        Classe createdClasse = classeService.ajouterClasse(classe);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPrenom("Sami");
        utilisateur.setNom("Ben Ali");
        utilisateur.setPassword("pwd");
        Utilisateur createdUser = utilisateurService.ajouterUtilisateur(utilisateur);

        CoursClassroom cours = new CoursClassroom();
        cours.setNom("Java");
        cours.setSpecialite(Specialite.INFORMATIQUE);
        cours.setNbHeures(30);
        cours.setArchive(false);
        CoursClassroom createdCours = coursClassroomService.ajouterCoursClassroom(cours, createdClasse.getCodeClasse());

        classe.setTitre("3INFO2");
        Classe updatedClasse = classeService.updateClasse(createdClasse.getCodeClasse(), classe);
        assertEquals("3INFO2", updatedClasse.getTitre());
        assertTrue(classeService.getAllClasses().stream().anyMatch(c -> c.getCodeClasse().equals(createdClasse.getCodeClasse())));

        utilisateur.setPrenom("Samiya");
        utilisateur.setClasse(createdClasse);
        Utilisateur updatedUser = utilisateurService.updateUtilisateur(createdUser.getIdUtilisateur(), utilisateur);
        assertEquals("Samiya", updatedUser.getPrenom());
        assertTrue(utilisateurService.getAllUtilisateurs().stream().anyMatch(u -> u.getIdUtilisateur().equals(createdUser.getIdUtilisateur())));

        cours.setNom("Java Avance");
        cours.setClasse(createdClasse);
        CoursClassroom updatedCours = coursClassroomService.updateCoursClassroom(createdCours.getIdCours(), cours);
        assertEquals("Java Avance", updatedCours.getNom());
        assertTrue(coursClassroomService.getAllCoursClassrooms().stream().anyMatch(c -> c.getIdCours().equals(createdCours.getIdCours())));

        coursClassroomService.deleteCoursClassroom(createdCours.getIdCours());
        utilisateurService.deleteUtilisateur(createdUser.getIdUtilisateur());
        classeService.deleteClasse(createdClasse.getCodeClasse());
    }
}
