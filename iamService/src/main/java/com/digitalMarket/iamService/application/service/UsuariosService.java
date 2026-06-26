package com.digitalMarket.iamService.application.service;

import com.digitalMarket.iamService.application.dto.request.UsuariosRequest;
import com.digitalMarket.iamService.application.dto.response.UsuariosLoadResponse;
import com.digitalMarket.iamService.application.dto.response.UsuariosResponse;
import com.digitalMarket.iamService.domain.roles.Roles;
import com.digitalMarket.iamService.domain.usuario.Usuarios;
import com.digitalMarket.iamService.domain.usuario.UsuariosRepository;
import com.digitalMarket.iamService.application.abstractService.IUsuariosService;
import com.digitalMarket.iamService.infraestructure.enums.Tables;
import com.digitalMarket.iamService.infraestructure.exceptions.IdNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.stream.Collectors;

@Transactional
@Service
public class UsuariosService implements IUsuariosService {

    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;


    public UsuariosService(UsuariosRepository usuariosRepository, PasswordEncoder passwordEncoder) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsuariosResponse create(UsuariosRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (usuariosRepository.findByEmail(request.getEmail()) != null) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }else{
            var userToPersist = Usuarios.builder()
                    .nombre(request.getNombre())
                    .apellido(request.getApellido())
                    .email(request.getEmail())
                    .passwordHash(passwordEncoder.encode(request.getPasswordHash()))
                    .activo(true)
                    .fechaCreacion(Instant.now())
                    .build();

            var userPersisted=usuariosRepository.save(userToPersist);
            return this.entityToResponse(userPersisted);
        }
    }

    @Override
    public UsuariosResponse read(Long id) {
        var userToRead= usuariosRepository.findById(id).orElseThrow(()->new IdNotFoundException(Tables.usuarios.name()));
        return this.entityToResponse(userToRead);
    }


    public UsuariosLoadResponse readWithRoles(Long id) {
        var userToRead= usuariosRepository.findById(id).orElseThrow(()->new IdNotFoundException(Tables.usuarios.name()));
        return this.entityToLoadResponse(userToRead);
    }

    @Override
    public UsuariosResponse update(UsuariosRequest request, Long idUsuario) {
        var userToUpdate = usuariosRepository.findById(idUsuario).orElseThrow(()->new IdNotFoundException(Tables.usuarios.name()));
      //  var rolUsuario = rolRepository.findById(request.getIdRol()).orElseThrow(()->new IdNotFoundException(Tables.rol.name()));
        userToUpdate.setNombre(request.getNombre());
        userToUpdate.setApellido(request.getApellido());
        userToUpdate.setEmail(request.getEmail());
        userToUpdate.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));
        userToUpdate.setActivo(true);
        userToUpdate.setFechaCreacion(Instant.now());
        var userPersisted= usuariosRepository.save(userToUpdate);
        UsuariosResponse usuariosResponse;
        usuariosResponse = entityToResponse(userPersisted);
        return usuariosResponse;
    }

    @Override
    public void delete(Long id) {
        var userToDelete = usuariosRepository.findById(id).orElseThrow(()->new IdNotFoundException(Tables.usuarios.name()));
        this.usuariosRepository.delete(userToDelete);
    }


    private UsuariosResponse entityToResponse(Usuarios entity) {
        var response = new UsuariosResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }


    private UsuariosLoadResponse entityToLoadResponse(Usuarios entity) {
        return UsuariosLoadResponse.builder()
                .id(entity.getId().longValue())
                .nombre(entity.getNombre())
                .apellido(entity.getApellido())
                .email(entity.getEmail())
                .activo(entity.getActivo())
                .roles(
                        entity.getRoles()
                                .stream()
                                .map(Roles::getNombre)
                                .collect(Collectors.toSet())
                )
                .build();
    }
}