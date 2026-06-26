package com.digitalMarket.iamService.application.service;

import com.digitalMarket.iamService.application.dto.request.PermisosRequest;
import com.digitalMarket.iamService.application.dto.response.PermisosResponse;
import com.digitalMarket.iamService.application.dto.response.UsuariosLoadResponse;
import com.digitalMarket.iamService.domain.permisos.Permisos;
import com.digitalMarket.iamService.domain.permisos.PermisosRepository;
import com.digitalMarket.iamService.application.abstractService.IPermisosService;
import com.digitalMarket.iamService.infraestructure.enums.Tables;
import com.digitalMarket.iamService.infraestructure.exceptions.IdNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class PermisosService implements IPermisosService {

    private final PermisosRepository permisosRepository;

    public PermisosService(PermisosRepository permisosRepository) {
        this.permisosRepository = permisosRepository;
    }

    @Override
    public PermisosResponse create(@Valid PermisosRequest request){
        if (request == null){
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (permisosRepository.findByNombre(request.getNombre()).isPresent()){
            throw new IllegalArgumentException("El permiso ya está registrado");
        }else {
            var permissionPersist = Permisos.builder()
                    .nombre(request.getNombre())
                    .descripcion(request.getDescripcion())
                    .build();

            var permissionPersisted = permisosRepository.save(permissionPersist);
            return this.entityToResponse(permissionPersisted);
        }
    }

    @Override
    public PermisosResponse read(Long id) {
        var permisosToRead = this.permisosRepository.findById(id).orElseThrow(()->new IdNotFoundException(Tables.permisos.name()));
        return this.entityToResponse(permisosToRead);
    }

    public List<PermisosResponse> readByIdRol(Long idRole) {
        return this.permisosRepository.findPermisosEntitiesByIdRol(idRole)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PermisosResponse update(PermisosRequest request, Long id) {
        var permisoToUpdate= permisosRepository.findById(id).orElseThrow(()->new IdNotFoundException(Tables.permisos.name()));
        permisoToUpdate.setNombre(request.getNombre());
        permisoToUpdate.setDescripcion(request.getDescripcion());
        var permisosPersisted=permisosRepository.save(permisoToUpdate);
        PermisosResponse permisosResponse;
        permisosResponse = entityToResponse(permisosPersisted);
        return permisosResponse;
    }

    @Override
    public void delete(Long id) {
        var permisosToDelete = permisosRepository.findById(id).orElseThrow(()->new IdNotFoundException(Tables.permisos.name()));
        this.permisosRepository.delete(permisosToDelete);
    }

    private PermisosResponse entityToResponse(Permisos entity) {
        var response = new PermisosResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}