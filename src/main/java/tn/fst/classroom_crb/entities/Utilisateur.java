package tn.fst.classroom_crb.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

// Indique que la classe est une entité JPA mappée à une table
 @Entity
// Spécifie le nom de la table en base de données
 @Table(name = "utilisateur")
@Getter
@Setter
public class Utilisateur {

    // Indique la colonne clé primaire
     @Id
    // Stratégie de génération de la clé primaire
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUtilisateur;

    private String prenom;

    private String nom;

    private String password;

    // Association JPA: plusieurs -> un
     @ManyToOne
    // Spécifie la colonne de jointure pour l'association
     @JoinColumn(name = "code_classe")
    // Ignore des propriétés spécifiques lors de la sérialisation JSON
     @JsonIgnoreProperties({"utilisateurs", "coursClassrooms"})
    private Classe classe;
}
