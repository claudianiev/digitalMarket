package com.digitalMarket.iamService.domain.roles.valueObject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class RolPermisoId implements java.io.Serializable {
    private static final long serialVersionUID = 7482816875694310563L;
    @NotNull
    @Column(name = "rol_id", nullable = false)
    private Integer rolId;

    @NotNull
    @Column(name = "permiso_id", nullable = false)
    private Integer permisoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RolPermisoId entity = (RolPermisoId) o;
        return Objects.equals(this.rolId, entity.rolId) &&
                Objects.equals(this.permisoId, entity.permisoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rolId, permisoId);
    }

}