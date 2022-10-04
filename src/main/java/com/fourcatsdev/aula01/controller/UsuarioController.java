package com.fourcatsdev.aula01.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fourcatsdev.aula01.model.Papel;
import com.fourcatsdev.aula01.model.Usuario;
import com.fourcatsdev.aula01.repository.PapelRepository;
import com.fourcatsdev.aula01.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(path = "/usuario")
@RequiredArgsConstructor
public class UsuarioController {

  private final UsuarioRepository usuarioRepository;
  private final PapelRepository papelRepository;
  private final BCryptPasswordEncoder criptografia;

  private boolean temAutorizacao(Usuario usuario, String papel) {
    for (Papel pp : usuario.getPapeis()) {
      if (pp.getPapel().equals(papel)) {
        return true;
      }
    }
    return false;
  }

  @GetMapping(path = "/index")
  public String index(@CurrentSecurityContext(expression = "authentication.name") String login) {
    Usuario usuario = usuarioRepository.findByLogin(login);
    String redirectURL = "";
    if (temAutorizacao(usuario, "ADMIN")) {
      redirectURL = "/auth/admin/admin-index";
    } else if (temAutorizacao(usuario, "USER")) {
      redirectURL = "/auth/user/user-index";
    } else if (temAutorizacao(usuario, "BIBLIOTECARIO")) {
      redirectURL = "/auth/biblio/biblio-index";
    }
    return redirectURL;
  }

  @GetMapping(path = "/novo")
  public String adicionarUsuario(Model model) {
    model.addAttribute("usuario", new Usuario());
    return "/publica-criar-usuario";
  }

  @PostMapping(path = "/salvar")
  public String salvarUsuario(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes,
      Model model) {
    if (result.hasErrors()) {
      return "/publica-criar-usuario";
    }
    Usuario usr = usuarioRepository.findByLogin(usuario.getLogin());
    if (usr != null) {
      model.addAttribute("loginExiste", "Login já existe cadastrado.");
      return "/publica-criar-usuario";
    }

    usuario.getPapeis().add(papelRepository.findByPapel("USER"));
    String senhaCriptografada = criptografia.encode(usuario.getPassword());
    usuario.setPassword(senhaCriptografada);
    usuarioRepository.save(usuario);
    attributes.addFlashAttribute("mensagem", "Usuario salvo com sucesso!");
    return "redirect:/usuario/novo";

  }

  @GetMapping(path = "/admin/listar")
  public String listarUsuario(Model model) {
    model.addAttribute("usuarios", usuarioRepository.findAll());
    return "/auth/admin/admin-listar-usuario";
  }

  @GetMapping("/admin/apagar/{id}")
  public String deleteUser(@PathVariable Long id, Model model) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id inválido :" + id));
    usuarioRepository.delete(usuario);
    return "redirect:/usuario/admin/listar";
  }

  @GetMapping(path = "/editar/{id}")
  public String editarUsuario(@PathVariable Long id, Model model) {
    Optional<Usuario> usuarioVelho = usuarioRepository.findById(id);
    if (!usuarioVelho.isPresent()) {
      throw new IllegalArgumentException("Usuario Inválido: " + id);
    }
    Usuario usuario = usuarioVelho.get();
    model.addAttribute("usuario", usuario);
    return "/auth/user/user-alterar-usuario";
  }

  @PostMapping(path = "/editar/{id}")
  public String editarUsuario(@PathVariable Long id, @Valid Usuario usuario, BindingResult result) {
    if (result.hasErrors()) {
      usuario.setId(id);
      return "/auth/user/user-alterar-usuario";
    }
    usuarioRepository.save(usuario);
    return "redirect:/usuario/admin/listar";
  }

  @GetMapping(path = "/editarPapel/{id}")
  public String selecionarPapel(@PathVariable Long id, Model model) {
    Optional<Usuario> usuarioVelho = usuarioRepository.findById(id);
    if (!usuarioVelho.isPresent()) {
      throw new IllegalArgumentException("Usuário inválido: " + id);
    }

    Usuario usuario = usuarioVelho.get();
    model.addAttribute("usuario", usuario);
    model.addAttribute("listaPapeis", papelRepository.findAll());
    return "/auth/admin/admin-editar-papel-usuario";

  }

  @PostMapping("/editarPapel/{id}")
  public String atribuirPapel(@PathVariable("id") long idUsuario,
      @RequestParam(value = "pps", required = false) int[] pps,
      Usuario usuario,
      RedirectAttributes attributes) {
    if (pps == null) {
      usuario.setId(idUsuario);
      attributes.addFlashAttribute("mensagem", "Pelo menos um papel deve ser informado");
      return "redirect:/usuario/editarPapel/" + idUsuario;
    } else {
      // Obtém a lista de papéis selecionada pelo usuário do banco
      List<Papel> papeis = new ArrayList<Papel>();
      for (int i = 0; i < pps.length; i++) {
        long idPapel = pps[i];
        Optional<Papel> papelOptional = papelRepository.findById(idPapel);
        if (papelOptional.isPresent()) {
          Papel papel = papelOptional.get();
          papeis.add(papel);
        }
      }
      Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);
      if (usuarioOptional.isPresent()) {
        Usuario usr = usuarioOptional.get();
        usr.setPapeis(papeis); // relaciona papéis ao usuário
        usr.setAtivo(usuario.isAtivo());
        usuarioRepository.save(usr);
      }
    }
    return "redirect:/usuario/admin/listar";
  }

}
