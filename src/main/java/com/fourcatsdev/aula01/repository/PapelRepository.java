package com.fourcatsdev.aula01.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fourcatsdev.aula01.model.Papel;

public interface PapelRepository extends JpaRepository<Papel, Long> {

  Papel findByPapel(String string);

}
