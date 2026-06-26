package com.digitalMarket.iamService.application.abstractService;

import com.digitalMarket.iamService.application.dto.request.UsuariosRequest;
import com.digitalMarket.iamService.application.dto.response.UsuariosResponse;

public interface IUsuariosService extends CrudService<UsuariosRequest, UsuariosResponse,Long>{
    UsuariosResponse create(UsuariosRequest request);
}
