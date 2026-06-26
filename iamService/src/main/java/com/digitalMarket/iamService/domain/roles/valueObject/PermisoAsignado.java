package com.digitalMarket.iamService.domain.roles.valueObject;

import com.digitalMarket.iamService.domain.permisos.Permisos;
import com.digitalMarket.iamService.domain.roles.Roles;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
//@Data
public class PermisoAsignado implements Serializable {

    @EmbeddedId
    private RolPermisoId id;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "rol_id", nullable = false)
    private Roles rol;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "permiso_id", nullable = false)
    private Permisos permiso;
}
