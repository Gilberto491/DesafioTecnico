package com.sicredi.desafio.repository;

import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.model.enumerations.StatusPautaEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {
    List<Pauta> findAllByStatus(StatusPautaEnum status);
}
