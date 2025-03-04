package tech.bnpl.apionline.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "esquema_pagos")
public class EsquemaPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "numero_pagos", nullable = false)
    private Integer numeroPagos;

    @Column(name = "frecuencia_cobro", nullable = false)
    private String frecuenciaCobro;

    @Column(nullable = false, columnDefinition = "NUMERIC(10,2)")
    private Double tasa;

    @Column(name = "habilitado", nullable = false)
    private Boolean habilitado;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "esquema_condicion",
            joinColumns = @JoinColumn(name = "id_esquema"),
            inverseJoinColumns = @JoinColumn(name = "id_condicion")
    )
    private List<CondicionRegla> condiciones;

}
