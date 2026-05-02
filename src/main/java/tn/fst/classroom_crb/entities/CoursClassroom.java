package tn.fst.classroom_crb.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cours_classroom")
@Getter
@Setter
public class CoursClassroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCours;

    @Enumerated(EnumType.STRING)
    private Specialite specialite;

    private String nom;

    private Integer nbHeures;

    private boolean archive;

    @ManyToOne
    @JoinColumn(name = "code_classe")
    @JsonIgnoreProperties({"utilisateurs", "coursClassrooms"})
    private Classe classe;
}
