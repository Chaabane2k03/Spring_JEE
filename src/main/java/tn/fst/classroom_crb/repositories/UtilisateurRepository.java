package tn.fst.classroom_crb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.fst.classroom_crb.entities.Niveau;
import tn.fst.classroom_crb.entities.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Integer countByClasseNiveau(Niveau niveau);
}
