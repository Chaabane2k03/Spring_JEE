package tn.fst.classroom_crb.services.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.fst.classroom_crb.entities.Classe;
import tn.fst.classroom_crb.repositories.ClasseRepository;
import tn.fst.classroom_crb.services.IClasseService;

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
public class ClasseServiceImpl implements IClasseService {

    private final ClasseRepository classeRepository;

    @Override
    public Classe ajouterClasse(Classe c) {
        return classeRepository.save(c);
    }

    @Override
    @Transactional(readOnly = true)
    public Classe getClasseById(Integer codeClasse) {
        return classeRepository.findById(codeClasse)
                .orElseThrow(() -> new IllegalArgumentException("Classe introuvable avec code " + codeClasse));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Classe> getAllClasses() {
        return classeRepository.findAll();
    }

    @Override
    public Classe updateClasse(Integer codeClasse, Classe classe) {
        Classe existing = classeRepository.findById(codeClasse)
                .orElseThrow(() -> new IllegalArgumentException("Classe introuvable avec code " + codeClasse));
        existing.setTitre(classe.getTitre());
        existing.setNiveau(classe.getNiveau());
        return classeRepository.save(existing);
    }

    @Override
    public void deleteClasse(Integer codeClasse) {
        if (!classeRepository.existsById(codeClasse)) {
            throw new IllegalArgumentException("Classe introuvable avec code " + codeClasse);
        }
        classeRepository.deleteById(codeClasse);
    }
}
