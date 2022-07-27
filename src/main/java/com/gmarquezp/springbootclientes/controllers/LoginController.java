package com.gmarquezp.springbootclientes.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class LoginController {

    @GetMapping("/login")
    // Principal // Obtiene el objeto de Spring Security para controlar la autenticacion
    public String login(
                        @RequestParam(name = "error", required = false) String error, // El posible pathParam que envia SprinGSecurity si falla el login
                        Model model,
                        Principal principal,
                        RedirectAttributes flash) {

        if(principal != null){
            flash.addFlashAttribute("messageSuccess", "Ya has iniciado sesion");
            return "redirect:/clientes/";
        }


        if(error != null){
            System.out.println("Error de login");
            flash.addFlashAttribute("messageDanger", "Usuario o contraseña invalido wey =/!");
            return "redirect:/login";
        }

        model.addAttribute("titulo", "Login");
        return "login";
    }
}
