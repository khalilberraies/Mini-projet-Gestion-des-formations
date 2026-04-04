// FormationRepository.java
package com.isi.gestion_formation.repository;
import com.isi.gestion_formation.entity.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface FormationRepository extends JpaRepository<Formation, Long> {
    List<Formation> findByAnnee(Integer annee);
    long countByAnnee(Integer annee);
}