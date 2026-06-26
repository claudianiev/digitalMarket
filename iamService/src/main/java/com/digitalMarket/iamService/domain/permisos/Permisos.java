package com.digitalMarket.iamService.domain.permisos;

import com.digitalMarket.iamService.domain.roles.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="permisos")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Permisos implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permisos_seq_gen")
    @SequenceGenerator(name = "permisos_seq_gen", sequenceName = "permisos_permiso_id_seq", allocationSize = 1)

    @Column(name = "permiso_id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion", length = Integer.MAX_VALUE)
    private String descripcion;

    @ManyToMany(mappedBy = "permisos")
    private Set<Roles> roles = new LinkedHashSet<>();
}
