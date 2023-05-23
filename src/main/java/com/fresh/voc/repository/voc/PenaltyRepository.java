package com.fresh.voc.repository.voc;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fresh.voc.model.voc.Penalty;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

	@Query("SELECT p FROM Penalty p WHERE p.id = :id AND p.voc.id = :vocId")
	Optional<Penalty> findByIdAndVocId(Long id, @Param("vocId") Long vocId);
}
