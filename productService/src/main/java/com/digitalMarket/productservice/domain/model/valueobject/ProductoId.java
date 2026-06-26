package com.digitalMarket.productservice.domain.model.valueobject;

import java.util.UUID;

public class ProductoId {
    private final UUID valor;

    public ProductoId(UUID valor) {
        this.valor = valor;
    }

    public static ProductoId of(UUID valor) {
        return new ProductoId(valor);
    }

    public UUID getValor() {
        return valor;
    }
}