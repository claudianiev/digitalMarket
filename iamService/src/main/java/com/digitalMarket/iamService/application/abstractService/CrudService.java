package com.digitalMarket.iamService.application.abstractService;


import com.digitalMarket.iamService.application.dto.response.UsuariosLoadResponse;

public interface CrudService <RQ,RS,ID>{
    RS create(RQ request);

    RS read(ID id);

    RS update(RQ request, ID id);

    void delete(ID id);
}