package com.sicredi.desafio.repository;

import com.sicredi.desafio.model.PautaOpcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PautaOpcaoRepository extends JpaRepository<PautaOpcao, Long> {
    List<PautaOpcao> findByPautaId(Long pautaId);
}
