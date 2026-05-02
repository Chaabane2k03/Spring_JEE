package tn.fst.classroom_crb.services.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.fst.classroom_crb.entities.Classe;
import tn.fst.classroom_crb.entities.Niveau;
import tn.fst.classroom_crb.entities.Utilisateur;
import tn.fst.classroom_crb.repositories.ClasseRepository;
import tn.fst.classroom_crb.repositories.UtilisateurRepository;
import tn.fst.classroom_crb.services.IUtilisateurService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Retryable(
        retryFor = TransientDataAccessException.class,
        maxAttemptsExpression = "${app.retry.max-attempts:3}",
        backoff = @Backoff(
                delayExpression = "${app.retry.delay-ms:200}",
                multiplierExpression = "${app.retry.multiplier:2.0}",
                maxDelayExpression = "${app.retry.max-delay-ms:2000}"
        )
)
public class UtilisateurServiceImpl implements IUtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final ClasseRepository classeRepository;

    @Override
    public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public void affecterUtilisateurClasse(Integer idUtilisateur, Integer codeClasse) {
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable avec id " + idUtilisateur));
        Classe classe = classeRepository.findById(codeClasse)
                .orElseThrow(() -> new IllegalArgumentException("Classe introuvable avec code " + codeClasse));
        utilisateur.setClasse(classe);
        utilisateurRepository.save(utilisateur);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer nbUtilisateursParNiveau(Niveau nv) {
        return utilisateurRepository.countByClasseNiveau(nv);
    }

    @Override
    @Transactional(readOnly = true)
    public Utilisateur getUtilisateurById(Integer idUtilisateur) {
        return utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable avec id " + idUtilisateur));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    @Override
    public Utilisateur updateUtilisateur(Integer idUtilisateur, Utilisateur utilisateur) {
        Utilisateur existing = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable avec id " + idUtilisateur));
        existing.setPrenom(utilisateur.getPrenom());
        existing.setNom(utilisateur.getNom());
        existing.setPassword(utilisateur.getPassword());
        if (utilisateur.getClasse() != null && utilisateur.getClasse().getCodeClasse() != null) {
            Classe classe = classeRepository.findById(utilisateur.getClasse().getCodeClasse())
                    .orElseThrow(() -> new IllegalArgumentException("Classe introuvable avec code " + utilisateur.getClasse().getCodeClasse()));
            existing.setClasse(classe);
        }
        return utilisateurRepository.save(existing);
    }

    @Override
    public void deleteUtilisateur(Integer idUtilisateur) {
        if (!utilisateurRepository.existsById(idUtilisateur)) {
            throw new IllegalArgumentException("Utilisateur introuvable avec id " + idUtilisateur);
        }
        utilisateurRepository.deleteById(idUtilisateur);
    }
}
