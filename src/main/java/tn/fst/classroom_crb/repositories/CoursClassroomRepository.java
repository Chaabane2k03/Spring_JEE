package tn.fst.classroom_crb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.fst.classroom_crb.entities.CoursClassroom;
import tn.fst.classroom_crb.entities.Niveau;
import tn.fst.classroom_crb.entities.Specialite;

public interface CoursClassroomRepository extends JpaRepository<CoursClassroom, Integer> {

    // Déclare une requête JPQL personnalisée
     @Query("select coalesce(sum(c.nbHeures), 0) from CoursClassroom c where c.specialite = :sp and c.classe.niveau = :nv")
    Integer nbHeuresParSpecEtNiv(@Param("sp") Specialite specialite, @Param("nv") Niveau niveau);
}
