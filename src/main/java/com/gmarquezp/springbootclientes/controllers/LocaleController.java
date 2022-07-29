package com.gmarquezp.springbootclientes.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LocaleController {

    @GetMapping("/locale")
    public String locale(HttpServletRequest request) {

        // referer // Obtiene la url de la que se ha accedido a la actual
        String ultimaUrl = request.getHeader("referer");
        return "redirect:".concat(ultimaUrl); // redirecciona a la ultima url

    }

}
