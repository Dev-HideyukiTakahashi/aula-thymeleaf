package com.fourcatsdev.aula01.security;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fourcatsdev.aula01.model.Usuario;
import com.fourcatsdev.aula01.repository.UsuarioRepository;

@Service
@Transactional
public class DetailsUsuarioService implements UserDetailsService {

  private UsuarioRepository usuarioRepository;

  public DetailsUsuarioService(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Usuario usuario = usuarioRepository.findByLogin(username);

    if (usuario != null && usuario.isAtivo()) {
      DetalheUsuario detalheUsuario = new DetalheUsuario(usuario);
      return detalheUsuario;
    } else {
      throw new UsernameNotFoundException("Usuario não encontrado");
    }
  }

}
