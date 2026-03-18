package com.evalflow.eval_flow.repository;

import com.evalflow.eval_flow.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {

    List<Round> findByTechnologyId(Long technologyId);
}