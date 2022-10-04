package com.fourcatsdev.aula01.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fourcatsdev.aula01.repository.UsuarioRepository;

@Configuration
@EnableWebMvcSecurity
public class ConfigSecurity
    extends WebSecurityConfigurerAdapter {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private LoginSucesso loginSucesso;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/").permitAll() // todos usuários tem acesso a page root
        .antMatchers("/auth/user/*").hasAnyAuthority("USER", "ADMIN", "BIBLIOTECARIO")
        .antMatchers("/auth/admin/*").hasAnyAuthority("ADMIN")
        .antMatchers("/auth/biblio/*").hasAnyAuthority("BIBLIOTECARIO")
        .antMatchers("/usuario/admin/*").hasAnyAuthority("ADMIN")
        .and()
        .exceptionHandling().accessDeniedPage("/auth/auth-acesso-negado")
        .and()
        .formLogin().successHandler(loginSucesso)
        .loginPage("/login").permitAll()
        .and()
        .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/").permitAll();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    UserDetailsService detailsService = userDetailsServiceBean();
    BCryptPasswordEncoder criptografia = gerarCriptografia();

    auth.userDetailsService(detailsService).passwordEncoder(criptografia);
  }

  @Bean
  public BCryptPasswordEncoder gerarCriptografia() {
    BCryptPasswordEncoder criptografia = new BCryptPasswordEncoder();
    return criptografia;
  }

  @Override
  public UserDetailsService userDetailsServiceBean() throws Exception {
    DetailsUsuarioService detailsUsuarioService = new DetailsUsuarioService(usuarioRepository);

    return detailsUsuarioService;
  }

}
