package com.gateway.servicesImp;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gateway.models.Role;
import com.gateway.models.User;
import com.gateway.repository.UserRepository;
import com.gateway.security.jwt.IJwtProvider;
import com.gateway.services.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	// BCryptPasswordEncoder es un algoritmo de hash de contraseñas que utiliza una
	// función de hash basada en Blowfish
	@Autowired
	private IJwtProvider iJwtProvider;

	@Override
	public User saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.USER);
		user.setFechaCreacion(LocalDateTime.now());
		System.out.println(user.toString());
		User userCreated = userRepository.save(user);
		//agregar el token para enviarlo al cliente
		String jwt = iJwtProvider.generateToken(userCreated);
		userCreated.setToken(jwt);

		return userCreated;
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Transactional
	@Override
	public void changeRole(Role newRole, String username) {
		userRepository.updateUserRole(username, newRole);
	}

	@Override
	@Transactional
	public void updteRole(Role newRole, Long id) {
	    // Aqui debo actualizar el rol k me envien con ese usuario    
	    Optional<User> userYes = userRepository.findById(id); // Buscar por nombre de usuario
	    if (userYes.isPresent()) {
	        System.out.println("si encontre al usuario k hay k actualizarlo:    " 
	            + userYes.get().getNombre() + "  **************************");
	        userYes.get().setRole(newRole); // Seteo el nuevo rol
	        User rolUserSave = userRepository.save(userYes.get());
	        System.out.println("rol de:" + "" + " actualizado");
	    }
	    // Validar si el usuario k intenta actualizar el rol esta con Rol ADMIN
	}


	@Override
	public User findByUsernameReturnToken(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("El usuario no existe:" + username));

		String jwt = iJwtProvider.generateToken(user);
		user.setToken(jwt);
		return user;
	}

}
