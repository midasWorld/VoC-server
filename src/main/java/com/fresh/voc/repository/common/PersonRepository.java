package com.fresh.voc.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fresh.voc.model.common.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
