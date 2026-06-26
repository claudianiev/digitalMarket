package com.digitalMarket.iamService.application.abstractService;

import com.digitalMarket.iamService.application.dto.request.RolRequest;
import com.digitalMarket.iamService.application.dto.response.RolResponse;


public interface IRolService extends CrudService<RolRequest, RolResponse, Integer> {
    RolResponse create(RolRequest request);
}
