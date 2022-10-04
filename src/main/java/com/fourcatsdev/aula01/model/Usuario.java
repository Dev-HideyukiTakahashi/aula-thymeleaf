package com.fourcatsdev.aula01.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres")
  private String nome;

  @CPF(message = "CPF inválido!")
  private String cpf;

  @Temporal(TemporalType.DATE)
  @DateTimeFormat(pattern = "dd/MM/yyyy")
  private Date dataNascimento;

  @Email(message = "Email inválido")
  private String email;

  @NotEmpty(message = "A senha deve ser informada")
  @Size(min = 3, message = "A senha deve ter no mínimo 3 caracteres")
  private String password;

  @NotEmpty(message = "O login deve ser informado")
  @Size(min = 3, message = "O login deve ter no minimo 4 caracteres")
  private String login;

  private boolean ativo;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "usuario_papel", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "papel_id"))
  List<Papel> papeis = new ArrayList<>();

}
