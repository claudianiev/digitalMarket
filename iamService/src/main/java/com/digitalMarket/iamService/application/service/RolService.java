package com.digitalMarket.iamService.application.service;

import com.digitalMarket.iamService.application.dto.request.RolRequest;
import com.digitalMarket.iamService.application.dto.response.RolResponse;
import com.digitalMarket.iamService.application.dto.response.UsuariosLoadResponse;
import com.digitalMarket.iamService.domain.roles.Roles;
import com.digitalMarket.iamService.domain.roles.RolRepository;
import com.digitalMarket.iamService.application.abstractService.IRolService;
import com.digitalMarket.iamService.infraestructure.enums.Tables;
import com.digitalMarket.iamService.infraestructure.exceptions.IdNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class RolService implements IRolService {

    public final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public RolResponse create(RolRequest request) {
        if (request == null){
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (rolRepository.findByNombre(request.getNombre()).isPresent()){
            throw new IllegalArgumentException("El rol ya está registrado");
        }else{
            var rolToPersist = Roles.builder()
                    .nombre(request.getNombre())
                    .descripcion(request.getDescripcion())
                    .build();
            var rolPersisted=rolRepository.save(rolToPersist);
            return this.entityToResponse(rolPersisted);
        }
    }

    @Override
    public RolResponse read(Integer id) {
        var rolToRead= this.rolRepository.findById(id).orElseThrow(()->new IdNotFoundException(Tables.rol.name()));
        return this.entityToResponse(rolToRead);
    }

    @Override
    public RolResponse update(RolRequest request, Integer id) {
        var rolToUpdate= this.rolRepository.findById(id).orElseThrow(()->new IdNotFoundException(Tables.rol.name()));
        rolToUpdate.setNombre(request.getNombre());
        rolToUpdate.setDescripcion(request.getDescripcion());
        var rolPersisted=this.rolRepository.save(rolToUpdate);
        RolResponse rolResponse;
        rolResponse = this.entityToResponse(rolPersisted);
        return rolResponse;
    }

    @Override
    public void delete(Integer id) {
        var rolToDelete = this.rolRepository.findById(id).orElseThrow(()->new IdNotFoundException(Tables.rol.name()));
        this.rolRepository.delete(rolToDelete);
    }

    private RolResponse entityToResponse(Roles entity) {
        var response = new RolResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}