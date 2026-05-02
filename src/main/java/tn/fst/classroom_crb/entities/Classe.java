package tn.fst.classroom_crb.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// Indique que la classe est une entité JPA mappée à une table
 @Entity
// Spécifie le nom de la table en base de données
 @Table(name = "classe")
@Getter
@Setter
public class Classe {

    // Indique la colonne clé primaire
     @Id
    // Stratégie de génération de la clé primaire
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codeClasse;

    private String titre;

    // Indique comment persister un enum (ex: STRING)
     @Enumerated(EnumType.STRING)
    private Niveau niveau;

    // Ignore la propriété lors de la sérialisation JSON
     @JsonIgnore
    // Association JPA: un -> plusieurs
     @OneToMany(mappedBy = "classe")
    private List<Utilisateur> utilisateurs = new ArrayList<>();

    // Ignore la propriété lors de la sérialisation JSON
     @JsonIgnore
    // Association JPA: un -> plusieurs
     @OneToMany(mappedBy = "classe")
    private List<CoursClassroom> coursClassrooms = new ArrayList<>();
}
