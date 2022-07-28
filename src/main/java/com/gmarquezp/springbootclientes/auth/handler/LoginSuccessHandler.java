package com.gmarquezp.springbootclientes.auth.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /*
     * Funcion que se ejecutara cuando sea success el login de Spring Security
     * */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        SessionFlashMapManager flashMapManager = new SessionFlashMapManager();

        FlashMap flashMap = new FlashMap();
        flashMap.put("messageSuccess", "Bienvenido " + authentication.getName());

        // Seteando el map de flash en la sesion
        flashMapManager.saveOutputFlashMap(flashMap, request, response);

        logger.info("=".repeat(100));
        logger.info("Usuario " + authentication.getName() + " ha iniciado sesion");

        super.onAuthenticationSuccess(request, response, chain, authentication);
    }
}
