package com.gateway.security;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gateway.models.User;

/**
 * Esta clase unifica la estructura User de spring security con mi data User de
 * mi db y tabla usuario propia de la empresa
 * 
 * @author Pablo Rosas
 */
public class UserPrincipal implements UserDetails {
	/*
	 * UserDetails interfaz que se utiliza para representar los detalles de un
	 * usuario autenticado en el sistema. Proporciona métodos para obtener
	 * información sobre el usuario, como su nombre de usuario, contraseña, roles y
	 * privilegios.
	 */
	
	private static final long serialVersionUID = 1L;
	// Agregar los datos k temgo personalizado en mi tabla de la BD lo k ocupe claro
	private Long id;
    private String username;
    transient private String password; // para no serializarlo y se envia al cliente se le pone transient
    transient private User user;
    private Set<GrantedAuthority> authorities; // Spring security a un role lo llama authority

	/**
	 * @author Pablo Rosas
	 * @param
	 * @return una coleccion de los roles
	 */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	public UserPrincipal() {
	}

	public UserPrincipal(Long id, String username, String password, User user, Set<GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.user = user;
		this.authorities = authorities;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(Set<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String toString() {
		return "UserPrincipal [id=" + id + ", username=" + username + ", authorities=" + authorities + "]";
	}
    
    
}
