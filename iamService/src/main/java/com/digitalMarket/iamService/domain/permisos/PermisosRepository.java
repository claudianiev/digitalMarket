package com.digitalMarket.iamService.domain.permisos;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermisosRepository extends JpaRepository<Permisos, Long> {
     @Query("SELECT p FROM Permisos p JOIN FETCH p.roles WHERE p.id = :idRol")
     List<Permisos> findPermisosEntitiesByIdRol(@Param("idRol")long idRol);

     Permisos findById(Integer id);

     Optional<Permisos> findByNombre(String nombre);
}
