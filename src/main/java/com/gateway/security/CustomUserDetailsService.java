package com.gateway.security;

import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gateway.models.User;
import com.gateway.services.IUserService;
import com.gateway.utils.SecurityUtils;



@Service

/**
 * Esta clase personalizada implementa la intefaz UserDetailsService que me
 * ayuda a recuperar los datos de un usuario desde una base de datos
 * 
 * @author Pablo Rosas
 */
public class CustomUserDetailsService implements UserDetailsService {// Pesoanliza los metodos de busqueda del
	                                                                 // esta interfaz
    @Autowired
    private IUserService iUserService;
	/**
	 * Este metodo loadUserByUsername, propio de la interfaz UserDetailsService lo
	 * que hace es recuperar los detalles del usuario desde una fuente de datos,
	 * como una base de datos, y crear un objeto "UserDetails" que representa al
	 * usuario.
	 * 
	 * @author Pablo Rosas
	 * @param nombre del usuario
	 * @return
	 */
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	/*
		 * Aser la busqueda de un usuario y luego setearlo dentro del contexto de mi app
		 * realizo mi busqueda de usuario y su data completa con mi metodo del servicio
		 * que se conecta con mi base de datos
		 */
    	
       User user = iUserService.findByUsername(username)
                .orElseThrow( () -> new UsernameNotFoundException("El usuario no fue encontrado:"+username));
    	/*
		 * Obtengo solo los roles k tiene El usuario puede tener un o varios roles los
		 * almaceno en una coleccion, donde no se repetiran los datos. vienen de la base datos
		 * Set<GrantedAuthority> authorities = Collections.singleton(SecurityUtils.convertToAuthority(user.getRole().name()));

		 */
        
        // Set<GrantedAuthority> authorities = Set.of(SecurityUtils.convertToAuthority(user.getRole().name()));
        Set<GrantedAuthority> authorities = Collections.singleton(SecurityUtils.convertToAuthority(user.getRole().name()));

        UserPrincipal userPrincipal = new UserPrincipal();
		/*
		 * tengo k aser es colocar setear el usuario actual k obtenga en el contexto
		 * colocarlo en sesion dentro de mi app back para que en el futuro los request
		 * que me envien los clientes pasen directamente y el cliente pueda obtener los
		 * recursos
		 */

		/* Seteo los valores nesesario al objeto userPricipal para devolverlo lleno */
    	userPrincipal.setUser(user); //Este user objeto viene de la base de datos
		userPrincipal.setId(user.getId());
		userPrincipal.setUsername(user.getUsername());
		userPrincipal.setPassword(user.getPassword());
		userPrincipal.setAuthorities(authorities);
//        
//        
        return userPrincipal;
  
    }
}
