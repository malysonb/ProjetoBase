package com.malysonb.projetoBase.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malysonb.projetoBase.model.auth.Role;

public interface RoleRepository  extends JpaRepository<Role, String>{
    
}
