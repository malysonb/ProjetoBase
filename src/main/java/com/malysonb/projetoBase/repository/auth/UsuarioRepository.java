package com.malysonb.projetoBase.repository.auth;

import java.util.Optional;

import com.malysonb.projetoBase.model.auth.Usuario;
import com.malysonb.projetoBase.repository.base.GenericRepository;

public interface UsuarioRepository extends GenericRepository<Usuario, Long>{
    
    Optional<Usuario> findByLogin(String login);

}
