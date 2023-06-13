package com.gateway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gateway.models.Role;
import com.gateway.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    //findBy + nombreCampo
	// metodo que busca a un usuario por el username
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("update User set role=:role where username=:username")
    void updateUserRole(@Param("username") String username, @Param("role") Role role);

    @Modifying
    @Query("update User set role=:role where username=:username")
    void updateRol(@Param("username") String username, @Param("role") Role role);
    
}
