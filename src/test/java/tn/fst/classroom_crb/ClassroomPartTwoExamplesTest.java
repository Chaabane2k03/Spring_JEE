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
import tn.fst.classroom_crb.repositories.ClasseRepository;
import tn.fst.classroom_crb.repositories.CoursClassroomRepository;
import tn.fst.classroom_crb.repositories.UtilisateurRepository;
import tn.fst.classroom_crb.services.IClasseService;
import tn.fst.classroom_crb.services.ICoursClassroomService;
import tn.fst.classroom_crb.services.IUtilisateurService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class ClassroomPartTwoExamplesTest {

    @Autowired
    private IUtilisateurService utilisateurService;

    @Autowired
    private IClasseService classeService;

    @Autowired
    private ICoursClassroomService coursClassroomService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ClasseRepository classeRepository;

    @Autowired
    private CoursClassroomRepository coursClassroomRepository;

    @Test
    void partieDeuxExamples_shouldPersistAndReturnExpectedResults() {
        coursClassroomRepository.deleteAllInBatch();
        utilisateurRepository.deleteAllInBatch();
        classeRepository.deleteAllInBatch();

        Utilisateur amna = new Utilisateur();
        amna.setPrenom("Amna");
        amna.setNom("Ammar");
        amna.setPassword("etudiant");
        amna = utilisateurService.ajouterUtilisateur(amna);

        Utilisateur ahmed = new Utilisateur();
        ahmed.setPrenom("Ahmed");
        ahmed.setNom("Slama");
        ahmed.setPassword("admin");
        ahmed = utilisateurService.ajouterUtilisateur(ahmed);

        Classe c4ag1 = new Classe();
        c4ag1.setTitre("4AG1");
        c4ag1.setNiveau(Niveau.QUATRIEME);
        c4ag1 = classeService.ajouterClasse(c4ag1);

        Classe c5em1 = new Classe();
        c5em1.setTitre("5EM1");
        c5em1.setNiveau(Niveau.CINQUIEME);
        c5em1 = classeService.ajouterClasse(c5em1);

        CoursClassroom progC = new CoursClassroom();
        progC.setSpecialite(Specialite.INFORMATIQUE);
        progC.setNom("Programmation C");
        progC.setNbHeures(42);
        progC.setArchive(false);
        progC = coursClassroomService.ajouterCoursClassroom(progC, c4ag1.getCodeClasse());

        CoursClassroom plantes = new CoursClassroom();
        plantes.setSpecialite(Specialite.AGRICULTURE);
        plantes.setNom("Plantes");
        plantes.setNbHeures(25);
        plantes.setArchive(false);
        plantes = coursClassroomService.ajouterCoursClassroom(plantes, c4ag1.getCodeClasse());

        CoursClassroom sciencesNat = new CoursClassroom();
        sciencesNat.setSpecialite(Specialite.AGRICULTURE);
        sciencesNat.setNom("Sciences Naturelles");
        sciencesNat.setNbHeures(40);
        sciencesNat.setArchive(false);
        sciencesNat = coursClassroomService.ajouterCoursClassroom(sciencesNat, c4ag1.getCodeClasse());

        utilisateurService.affecterUtilisateurClasse(amna.getIdUtilisateur(), c4ag1.getCodeClasse());
        utilisateurService.affecterUtilisateurClasse(ahmed.getIdUtilisateur(), c5em1.getCodeClasse());

        Integer nbUsersQuatrieme = utilisateurService.nbUtilisateursParNiveau(Niveau.QUATRIEME);
        assertEquals(1, nbUsersQuatrieme);

        coursClassroomService.desaffecterCoursClassroomClasse(plantes.getIdCours());
        CoursClassroom plantesAfterDesaffect = coursClassroomRepository.findById(plantes.getIdCours()).orElseThrow();
        assertNull(plantesAfterDesaffect.getClasse());

        Integer nbHeuresAgriQuatrieme = coursClassroomService.nbHeuresParSpecEtNiv(Specialite.AGRICULTURE, Niveau.QUATRIEME);
        assertEquals(40, nbHeuresAgriQuatrieme);

        coursClassroomService.archiverCoursClassrooms();
        assertTrue(coursClassroomRepository.findById(progC.getIdCours()).orElseThrow().isArchive());
        assertTrue(coursClassroomRepository.findById(sciencesNat.getIdCours()).orElseThrow().isArchive());
        assertTrue(coursClassroomRepository.findById(plantes.getIdCours()).orElseThrow().isArchive());

        assertNotNull(utilisateurRepository.findById(amna.getIdUtilisateur()).orElseThrow().getClasse());
        assertNotNull(utilisateurRepository.findById(ahmed.getIdUtilisateur()).orElseThrow().getClasse());
        assertFalse(coursClassroomRepository.findAll().isEmpty());
    }
}
