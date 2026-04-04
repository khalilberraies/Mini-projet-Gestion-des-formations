package com.isi.gestion_formation.service;

import com.isi.gestion_formation.entity.Participant;
import com.isi.gestion_formation.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public List<Participant> findAll() {
        return participantRepository.findAll();
    }

    public Participant findById(Long id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant non trouvé"));
    }

    public Participant save(Participant participant) {
        return participantRepository.save(participant);
    }

    public void delete(Long id) {
        participantRepository.deleteById(id);
    }

    // Stats : nombre de participants par année
    public long countByAnnee(Integer annee) {
        return participantRepository.countParticipantsByAnnee(annee);
    }

    // Stats : répartition par profil (pour graphique camembert)
    public Map<String, Long> repartitionParProfil() {
        Map<String, Long> result = new HashMap<>();
        participantRepository.countByProfil()
                .forEach(row -> result.put((String) row[0], (Long) row[1]));
        return result;
    }
}