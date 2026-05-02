package tn.fst.classroom_crb.services.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.fst.classroom_crb.entities.Classe;
import tn.fst.classroom_crb.entities.CoursClassroom;
import tn.fst.classroom_crb.entities.Niveau;
import tn.fst.classroom_crb.entities.Specialite;
import tn.fst.classroom_crb.repositories.ClasseRepository;
import tn.fst.classroom_crb.repositories.CoursClassroomRepository;
import tn.fst.classroom_crb.services.ICoursClassroomService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@Retryable(
        retryFor = TransientDataAccessException.class,
        maxAttemptsExpression = "${app.retry.max-attempts:3}",
        backoff = @Backoff(
                delayExpression = "${app.retry.delay-ms:200}",
                multiplierExpression = "${app.retry.multiplier:2.0}",
                maxDelayExpression = "${app.retry.max-delay-ms:2000}"
        )
)
public class CoursClassroomServiceImpl implements ICoursClassroomService {

    private final CoursClassroomRepository coursClassroomRepository;
    private final ClasseRepository classeRepository;

    @Override
    public CoursClassroom ajouterCoursClassroom(CoursClassroom cc, Integer codeClasse) {
        Classe classe = classeRepository.findById(codeClasse)
                .orElseThrow(() -> new IllegalArgumentException("Classe introuvable avec code " + codeClasse));
        cc.setClasse(classe);
        return coursClassroomRepository.save(cc);
    }

    @Override
    public void desaffecterCoursClassroomClasse(Integer idCours) {
        CoursClassroom coursClassroom = coursClassroomRepository.findById(idCours)
                .orElseThrow(() -> new IllegalArgumentException("CoursClassroom introuvable avec id " + idCours));
        coursClassroom.setClasse(null);
        coursClassroomRepository.save(coursClassroom);
    }

    @Override
    @Scheduled(fixedRate = 60000)
    public void archiverCoursClassrooms() {
        List<CoursClassroom> cours = coursClassroomRepository.findAll();
        for (CoursClassroom cc : cours) {
            if (!cc.isArchive()) {
                cc.setArchive(true);
                log.info("Archivage automatique du cours: {}", cc.getNom());
                
                if (cc.getClasse() != null) {
                    cc.getClasse().getUtilisateurs().forEach(u -> 
                        log.info("NOTIFICATION: Utilisateur {} {}, le cours {} de votre classe {} a été archivé.", 
                            u.getPrenom(), u.getNom(), cc.getNom(), cc.getClasse().getTitre())
                    );
                }
            }
        }
        coursClassroomRepository.saveAll(cours);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer nbHeuresParSpecEtNiv(Specialite sp, Niveau nv) {
        return coursClassroomRepository.nbHeuresParSpecEtNiv(sp, nv);
    }

    @Override
    @Transactional(readOnly = true)
    public CoursClassroom getCoursClassroomById(Integer idCours) {
        return coursClassroomRepository.findById(idCours)
                .orElseThrow(() -> new IllegalArgumentException("CoursClassroom introuvable avec id " + idCours));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoursClassroom> getAllCoursClassrooms() {
        return coursClassroomRepository.findAll();
    }

    @Override
    public CoursClassroom updateCoursClassroom(Integer idCours, CoursClassroom coursClassroom) {
        CoursClassroom existing = coursClassroomRepository.findById(idCours)
                .orElseThrow(() -> new IllegalArgumentException("CoursClassroom introuvable avec id " + idCours));
        existing.setNom(coursClassroom.getNom());
        existing.setSpecialite(coursClassroom.getSpecialite());
        existing.setNbHeures(coursClassroom.getNbHeures());
        existing.setArchive(coursClassroom.isArchive());
        if (coursClassroom.getClasse() != null && coursClassroom.getClasse().getCodeClasse() != null) {
            Classe classe = classeRepository.findById(coursClassroom.getClasse().getCodeClasse())
                    .orElseThrow(() -> new IllegalArgumentException("Classe introuvable avec code " + coursClassroom.getClasse().getCodeClasse()));
            existing.setClasse(classe);
        }
        return coursClassroomRepository.save(existing);
    }

    @Override
    public void deleteCoursClassroom(Integer idCours) {
        if (!coursClassroomRepository.existsById(idCours)) {
            throw new IllegalArgumentException("CoursClassroom introuvable avec id " + idCours);
        }
        coursClassroomRepository.deleteById(idCours);
    }
}
