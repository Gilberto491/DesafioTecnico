package com.sicredi.desafio.repository;

import com.sicredi.desafio.model.PautaVotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaVotacaoRepository extends JpaRepository<PautaVotacao, Long> {
    boolean existsByAssociadoIdAndPautaOpcaoPautaId(Long associadoId, Long pautaOpcaoId);
}
