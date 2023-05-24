package com.fresh.voc.repository.voc;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fresh.voc.model.voc.Voc;

public interface VocRepository extends JpaRepository<Voc, Long> {

	@Query("SELECT v FROM Voc v LEFT JOIN FETCH v.penalty LEFT JOIN FETCH v.compensation WHERE v.id = :id")
	Optional<Voc> findWithPenaltyAndCompensationById(Long id);

	@Query("SELECT v FROM Voc v JOIN FETCH v.dueTarget LEFT JOIN FETCH v.penalty LEFT JOIN FETCH v.compensation")
	List<Voc> findAllWithPersonAndPenaltyAndCompensation();

	@Query("SELECT v FROM Voc v JOIN FETCH v.dueTarget LEFT JOIN FETCH v.penalty LEFT JOIN FETCH v.compensation WHERE v.id IN :ids")
	List<Voc> findAllWithPersonAndPenaltyAndCompensationByIdIn(List<Long> ids);
}
