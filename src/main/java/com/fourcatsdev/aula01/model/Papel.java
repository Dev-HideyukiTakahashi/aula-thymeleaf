package com.fourcatsdev.aula01.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;

@Entity
@Data
public class Papel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String papel;

  @ManyToMany(mappedBy = "papeis", fetch = FetchType.EAGER)
  List<Usuario> Usuario = new ArrayList<>();
}
