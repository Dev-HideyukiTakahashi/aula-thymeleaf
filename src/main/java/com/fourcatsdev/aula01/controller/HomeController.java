package com.fourcatsdev.aula01.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

  @RequestMapping(path = "/")
  public String hello(Model model) { // model é equivalente ao HttpServletRequest request
    model.addAttribute("msnBemVindo", "Bem vindo a Biblioteca!"); // requivalente ao request.setAttribute
    return "publica-index";
  }

  @RequestMapping(path = "/login")
  public String login() {

    return "login";
  }
}
