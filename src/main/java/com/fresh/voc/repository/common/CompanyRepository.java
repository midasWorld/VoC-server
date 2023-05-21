package com.fresh.voc.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fresh.voc.model.common.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
