package com.digitalMarket.iamService.domain.roles;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends CrudRepository<Roles, Integer> {
    Optional<Roles> findByRolId(Integer rolId);
    Optional<Roles> findByNombre(String nombre);
}