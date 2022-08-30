package com.ai21.Assignment1.repository;

import com.ai21.Assignment1.domain.Calculator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CalculatorRepository extends JpaRepository<Calculator, Long> {
    Calculator save(Calculator calculator);

    Page<Calculator> findByUsername(Pageable pageable, String username);

    @EntityGraph(attributePaths = "authorities")
    Optional<Calculator> findOneWithAuthoritiesByUsername(String username);
}
