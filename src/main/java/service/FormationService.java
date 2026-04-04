package com.isi.gestion_formation.service;

import com.isi.gestion_formation.entity.Formation;
import com.isi.gestion_formation.entity.Participant;
import com.isi.gestion_formation.repository.FormationRepository;
import com.isi.gestion_formation.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FormationService {

    private final FormationRepository formationRepository;
    private final ParticipantRepository participantRepository;

    public List<Formation> findAll() {
        return formationRepository.findAll();
    }

    public Formation findById(Long id) {
        return formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée"));
    }

    public Formation save(Formation formation) {
        return formationRepository.save(formation);
    }

    public void delete(Long id) {
        formationRepository.deleteById(id);
    }

    public List<Formation> findByAnnee(Integer annee) {
        return formationRepository.findByAnnee(annee);
    }

    // Ajouter un participant à une formation (max 4 formations par participant)
    public Participant ajouterParticipant(Long formationId, Long participantId) {
        Formation formation = findById(formationId);
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé"));

        if (participant.getFormations().size() >= 4) {
            throw new RuntimeException("Ce participant a déjà atteint le maximum de 4 formations !");
        }

        if (!participant.getFormations().contains(formation)) {
            participant.getFormations().add(formation);
            participantRepository.save(participant);
        }
        return participant;
    }
}