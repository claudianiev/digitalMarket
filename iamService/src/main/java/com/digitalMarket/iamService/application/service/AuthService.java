package com.digitalMarket.iamService.application.service;

import com.digitalMarket.iamService.domain.permisos.Permisos;
import com.digitalMarket.iamService.domain.roles.Roles;
import com.digitalMarket.iamService.domain.usuario.Usuarios;
import com.digitalMarket.iamService.domain.roles.RolRepository;
import com.digitalMarket.iamService.domain.permisos.PermisosRepository;
import com.digitalMarket.iamService.domain.usuario.UsuariosRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class AuthService {

    private final UsuariosRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PermisosRepository permisoRepository;

    public AuthService(UsuariosRepository usuarioRepository, RolRepository rolRepository, PermisosRepository permisoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.permisoRepository = permisoRepository;
    }

    @Transactional
    public void asignarRolAUsuario(String email, Integer rolId) {
        Usuarios usuario =  Optional.ofNullable(usuarioRepository.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Roles rol = rolRepository.findByRolId(rolId)
                .orElseThrow(()->new RuntimeException("Rol no encontrado"));

        usuario.getRoles().add(rol);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void asignarPermisoARol(Integer rolId, Integer id) {
        Roles rol = rolRepository.findByRolId(rolId)
                .orElseThrow(()->new RuntimeException("Rol no encontrado"));

        Permisos permiso = Optional.ofNullable(permisoRepository.findById(id))
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));

        rol.getPermisos().add(permiso);
        rolRepository.save(rol);
    }
}