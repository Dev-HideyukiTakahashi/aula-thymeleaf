package com.fourcatsdev.aula01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fourcatsdev.aula01.model.Papel;
import com.fourcatsdev.aula01.repository.PapelRepository;

@Component
public class CarregadoraDados implements CommandLineRunner {

  @Autowired
  private PapelRepository papelRepository;

  @Override
  public void run(String... args) throws Exception {
    Papel p1 = new Papel();
    p1.setPapel("ADMIN");
    Papel p2 = new Papel();
    p2.setPapel("USER");
    Papel p3 = new Papel();
    p3.setPapel("BIBLIOTECARIO");

    // papelRepository.saveAll(Arrays.asList(p1, p2, p3));
  }

}
