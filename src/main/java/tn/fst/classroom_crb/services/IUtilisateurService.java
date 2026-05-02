package tn.fst.classroom_crb.services;

import tn.fst.classroom_crb.entities.Niveau;
import tn.fst.classroom_crb.entities.Utilisateur;

import java.util.List;

public interface IUtilisateurService {
    // Exigence énoncé (II.a) : ajouter un utilisateur.
    Utilisateur ajouterUtilisateur(Utilisateur utilisateur);

    // Exigence énoncé (II.d) : affecter un utilisateur à une classe.
    void affecterUtilisateurClasse(Integer idUtilisateur, Integer codeClasse);

    // Exigence énoncé (II.e) : calculer le nombre d'utilisateurs par niveau.
    Integer nbUtilisateursParNiveau(Niveau nv);

    Utilisateur getUtilisateurById(Integer idUtilisateur);

    List<Utilisateur> getAllUtilisateurs();

    Utilisateur updateUtilisateur(Integer idUtilisateur, Utilisateur utilisateur);

    void deleteUtilisateur(Integer idUtilisateur);
}
