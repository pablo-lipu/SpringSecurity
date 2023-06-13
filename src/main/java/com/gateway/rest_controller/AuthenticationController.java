package com.gateway.rest_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.models.User;
import com.gateway.services.IAuthenticationService;
import com.gateway.services.IUserService;


@RestController
@RequestMapping("api/authentication")
public class AuthenticationController {

    @Autowired
    private IAuthenticationService iAuthenticationService;

    @Autowired
    private IUserService iUserService;

    @PostMapping("sign-up")
    public ResponseEntity<?> signUp(@RequestBody User user)
    {
        if(iUserService.findByUsername(user.getUsername()).isPresent())
        {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if(iUserService.findByEmail(user.getEmail()).isPresent())
        {
        	System.out.println(" *************  ¡el usuario ya existe!  ********************");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(iUserService.saveUser(user), HttpStatus.CREATED);
    }

    @PostMapping("sign-in")
    public ResponseEntity<?> signIn(@RequestBody User user)
    {
    	System.out.println("Usuario resibido en la request: " + user);
    	//llamo al servicio de Authenticacion
    	                         //loguea el usuario y escribele un token
        return new ResponseEntity<>(iAuthenticationService.signInAndReturnJWT(user), HttpStatus.OK);
    }

}
