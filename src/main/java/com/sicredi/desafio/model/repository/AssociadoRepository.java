package com.sicredi.desafio.model.repository;

import com.sicredi.desafio.model.Associado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, Long> {
}
