package tn.fst.classroom_crb.services;

import tn.fst.classroom_crb.entities.CoursClassroom;
import tn.fst.classroom_crb.entities.Niveau;
import tn.fst.classroom_crb.entities.Specialite;

import java.util.List;

public interface ICoursClassroomService {
    // Exigence énoncé (II.c) : ajouter un cours et l'affecter à une classe.
    CoursClassroom ajouterCoursClassroom(CoursClassroom cc, Integer codeClasse);

    // Exigence énoncé (II.f) : désaffecter un cours de sa classe.
    void desaffecterCoursClassroomClasse(Integer idCours);

    // Exigence énoncé (II.g - bonus) : archiver périodiquement les cours.
    void archiverCoursClassrooms();

    // Exigence énoncé (II.h) : somme des heures par spécialité et niveau.
    Integer nbHeuresParSpecEtNiv(Specialite sp, Niveau nv);

    CoursClassroom getCoursClassroomById(Integer idCours);

    List<CoursClassroom> getAllCoursClassrooms();

    CoursClassroom updateCoursClassroom(Integer idCours, CoursClassroom coursClassroom);

    void deleteCoursClassroom(Integer idCours);
}
