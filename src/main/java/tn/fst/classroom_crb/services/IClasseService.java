package tn.fst.classroom_crb.services;

import tn.fst.classroom_crb.entities.Classe;

import java.util.List;

public interface IClasseService {
    // Exigence énoncé (II.b) : ajouter une classe.
    Classe ajouterClasse(Classe c);

    Classe getClasseById(Integer codeClasse);

    List<Classe> getAllClasses();

    Classe updateClasse(Integer codeClasse, Classe classe);

    void deleteClasse(Integer codeClasse);
}
