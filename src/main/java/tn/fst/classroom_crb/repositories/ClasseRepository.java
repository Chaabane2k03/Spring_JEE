package tn.fst.classroom_crb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.fst.classroom_crb.entities.Classe;
import tn.fst.classroom_crb.entities.Niveau;

public interface ClasseRepository extends JpaRepository<Classe, Integer> {
    long countByNiveau(Niveau niveau);
}
