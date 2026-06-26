package com.digitalMarket.iamService.domain.usuario.valueObject;

import com.digitalMarket.iamService.domain.roles.Roles;
import com.digitalMarket.iamService.domain.usuario.Usuarios;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
//@Data
public class RolAsignado implements Serializable {

    @EmbeddedId
    private UsuarioRolId id;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuario;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "rol_id", nullable = false)
    private Roles rol;
}
