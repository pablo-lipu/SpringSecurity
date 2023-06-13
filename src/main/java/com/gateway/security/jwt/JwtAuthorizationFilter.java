package com.gateway.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*El objetivo de crear un filter es capturar un request k me envia el cliente antees de k llegue a la logica del backend*/
/**
 * Captura el request k me envia el cliente evaluar si cumple com las reglas de
 * validacion k yo estableci y si es asi k continue su camino caso contrario
 * detener su peticion y enviar una exception al cliente indicando k el servidor
 * no puede procesar su peticion xk o cumple con mis reglas de validacion
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {
	/**
	 * para que esta inyeccion funcione nesesito crearle un metodo de tipo Bean que
	 * inicialice
	 */
    @Autowired
    private IJwtProvider iJwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = iJwtProvider.getAuthentication(request);
        if(authentication != null && iJwtProvider.isTokenValid(request))
        {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
