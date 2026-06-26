package com.digitalMarket.productservice.infrastructure.adapter.persistence;

import com.digitalMarket.productservice.application.dto.filter.ProductFilterRequest;
import com.digitalMarket.productservice.infrastructure.adapter.persistence.entity.ProductEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    private ProductSpecification() {
    }

    public static Specification<ProductEntity> build(
            ProductFilterRequest productFilter
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (productFilter.getNombre() != null &&
                    !productFilter.getNombre().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("nombre")),
                                "%" + productFilter.getNombre().toLowerCase() + "%"
                        )
                );
            }

            if (productFilter.getPrecioMin() != null) {

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("precio"),
                                productFilter.getPrecioMin()
                        )
                );
            }

            if (productFilter.getPrecioMax() != null) {

                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("precio"),
                                productFilter.getPrecioMax()
                        )
                );
            }

            if (productFilter.getActivo() != null) {

                predicates.add(
                        cb.equal(
                                root.get("activo"),
                                productFilter.getActivo()
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
