package com.fresh.voc.repository.voc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fresh.voc.model.voc.Compensation;

public interface CompensationRepository extends JpaRepository<Compensation, Long> {

	@Query("SELECT c FROM Compensation c JOIN FETCH c.voc")
	List<Compensation> findAllWithVoc();
}
