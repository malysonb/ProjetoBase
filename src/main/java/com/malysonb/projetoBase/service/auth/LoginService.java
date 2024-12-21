package com.malysonb.projetoBase.service.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.malysonb.projetoBase.config.JwtTokenUtil;
import com.malysonb.projetoBase.dto.auth.BearerToken;
import com.malysonb.projetoBase.dto.auth.RegistrarDTO;
import com.malysonb.projetoBase.exception.ValidacaoException;
import com.malysonb.projetoBase.model.auth.Usuario;
import com.malysonb.projetoBase.repository.auth.RoleRepository;
import com.malysonb.projetoBase.repository.auth.UsuarioRepository;
import com.malysonb.projetoBase.repository.base.GenericRepository;
import com.malysonb.projetoBase.service.base.ServiceGenerico;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LoginService extends ServiceGenerico<Usuario, Long> implements UserDetailsService{

    @Autowired
    UsuarioRepository uRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    private JwtTokenUtil jUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = uRepo.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não foi encontrado!"));
        return user;
    }

    public ResponseEntity<?> registrar(RegistrarDTO dto){
        if(uRepo.findByLogin(dto.getLogin()).isPresent()){
            return new ResponseEntity<>("Já existe um usuário com este login!", HttpStatus.SEE_OTHER);
        }
        else{
            Usuario user = new Usuario();
            user.setEmail(dto.getEmail());
            user.setNome(dto.getNome());
            user.setLogin(dto.getLogin());
            user.setSenha(new BCryptPasswordEncoder().encode(dto.getSenha()));
            user.setNivel(roleRepo.findById("ROLE_USER").get());
            user.setAtivado(true);
            uRepo.save(user);
            String token = jUtil.generateToken(user);
            return new ResponseEntity<>(new BearerToken(token, "Bearer "), HttpStatus.OK);
        }
    }

    public Usuario alterarUsuario(Long id, RegistrarDTO dto){
        Usuario user = uRepo.findById(id).orElseThrow(() -> new ValidacaoException("Usuário não encontrado"));
        user.setEmail(dto.getEmail());
        user.setNome(dto.getNome());
        user.setLogin(dto.getLogin());
        if(dto.getSenha() != null && !dto.getSenha().isBlank() && !dto.getSenha().isEmpty())
            user.setSenha(new BCryptPasswordEncoder().encode(dto.getSenha()));
        user.setNivel(roleRepo.findById("ROLE_USER").get());
        user.setAtivado(true);
        uRepo.save(user);
        return user;
    }

    public static Usuario getUser(){
        Usuario u = null;
        try {
            u = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new ValidacaoException("Usuário não está logado!");
        }
        return u;
    }
    
    @Override
    public GenericRepository<Usuario, Long> getRepository() {
        return uRepo;
    }

}
