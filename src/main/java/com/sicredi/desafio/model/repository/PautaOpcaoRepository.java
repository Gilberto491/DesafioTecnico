package com.sicredi.desafio.model.repository;

import com.sicredi.desafio.model.PautaOpcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaOpcaoRepository extends JpaRepository<PautaOpcao, Long> {
}
