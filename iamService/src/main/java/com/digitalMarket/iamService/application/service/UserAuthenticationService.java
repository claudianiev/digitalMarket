package com.digitalMarket.iamService.application.service;

import com.digitalMarket.iamService.domain.usuario.Usuarios;
import com.digitalMarket.iamService.domain.usuario.UsuariosRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserAuthenticationService implements UserDetailsService {

    private final UsuariosRepository usuarioRepository;

    public UserAuthenticationService(UsuariosRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

         Usuarios usuario =  Optional.ofNullable(usuarioRepository.findByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"+ email));
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPasswordHash())
                .authorities(
                        usuario.getRoles()
                                .stream()
                                .map(role -> new SimpleGrantedAuthority(role.getNombre()))
                                .toArray(SimpleGrantedAuthority[]::new)
                )
                .build();
    }



}
