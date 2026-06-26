package com.digitalMarket.iamService.domain.usuario;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuariosRepository extends CrudRepository<Usuarios, Long> {
    Usuarios findByEmail(String email);
}
