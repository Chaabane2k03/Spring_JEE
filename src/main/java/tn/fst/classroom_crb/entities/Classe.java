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

@Entity
@Table(name = "classe")
@Getter
@Setter
public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codeClasse;

    private String titre;

    @Enumerated(EnumType.STRING)
    private Niveau niveau;

    @JsonIgnore
    @OneToMany(mappedBy = "classe")
    private List<Utilisateur> utilisateurs = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "classe")
    private List<CoursClassroom> coursClassrooms = new ArrayList<>();
}
