// DomaineRepository.java
package com.isi.gestion_formation.repository;
import com.isi.gestion_formation.entity.Domaine;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DomaineRepository extends JpaRepository<Domaine, Long> {}