package com.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gateway.models.Role;
import com.gateway.security.jwt.JwtAuthorizationFilter;

/**
 * @author Pablo Rosas Clase que configura la autenticacion y el acceso a mis
 *         recurso segun mis roles y permisos
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {
	// Inyectar un objeto de mi clase personalizada que representa a un usuario de
		// mi app
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	/**
	 * Se encarga de la validacion de los usuarios
	 */

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public JwtAuthorizationFilter jwtAuthorizationFilter() {
		return new JwtAuthorizationFilter();
	}
	/**
	 * Configurar setear los recursos de libre acceso y cuales seran protegidos o
	 * logueadas en mi app
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
		// Crear una referencia
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
		// Crear una referencia
		AuthenticationManager authenticationManager = auth.build();
		// Configurar aquellos recursos k estaran publicos en mi app
		http.cors();
		http.csrf().disable();
		http.authenticationManager(authenticationManager);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeHttpRequests()
				.antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**/*",
						"/api/authentication/sign-in", "/api/authentication/sign-up").permitAll()
				
				 // Authorizo a poder listar a todos los user los productos
				/*.antMatchers(HttpMethod.GET, "/gateway/producto/all").permitAll()   */
				// Authorizo a podre crear a todos los user un producto															
			//	.antMatchers(HttpMethod.POST, "/gateway/producto/save").permitAll()
				// Solo los que tengan el rol Admin pueden acceder a esta ruta																
				//.antMatchers("/gateway/producto/**").hasRole(Role.ADMIN.name())
				
				//Crear un api k me permita actualizar usuarios  // /update-role/{role}
				.antMatchers("/api/user/change/{role}").hasRole(Role.ADMIN.name())
				
				/*Este path me permite actualizar un rol, solo usuarios con rol ADMIN lo podran aser*/
				.antMatchers("/api/user/update-role").hasRole(Role.ADMIN.name())
				
				.antMatchers("/api/user/current" ,"/api/user/list").hasRole(Role.ADMIN.name())
		
																				 
				.anyRequest().authenticated();

		http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}



}
