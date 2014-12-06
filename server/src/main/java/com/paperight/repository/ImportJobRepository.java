package com.paperight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paperight.product.ImportJob;

public interface ImportJobRepository extends JpaRepository<ImportJob, Long> {

}
