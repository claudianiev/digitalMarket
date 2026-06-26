package com.digitalMarket.productservice.infrastructure.rest;

import com.digitalMarket.productservice.application.dto.filter.ProductFilterRequest;
import com.digitalMarket.productservice.application.dto.request.ProductRequest;
import com.digitalMarket.productservice.application.dto.response.ProductResponse;
import com.digitalMarket.productservice.application.service.ProductService;
import com.digitalMarket.productservice.domain.model.Product;
import com.digitalMarket.productservice.infrastructure.adapter.persistence.mapper.ProductMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/products")
public class ProductController {


    private final ProductService service;
    private final ProductMapper mapper;

    public ProductController(ProductService service, ProductMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ProductResponse create(@RequestBody ProductRequest req) {

        Product product = service.create(
                req.nombre,
                req.descripcion,
                req.precio,
                req.tipoProducto,
                req.usuarioId
        );

        return mapper.toResponse(product);
    }

    @GetMapping
    public List<ProductResponse> getActivos() {
        return service.getActivos()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse  getById(@PathVariable Long id) {
        Product product = service.getById(id);
        return mapper.toResponse(product);
    }

    @PatchMapping("/{id}/desactivar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desactivar(@PathVariable Long id){

        service.desactivar(id);
    }

    @GetMapping("/filtro")
    public ResponseEntity<Page<ProductResponse>> search(

            @RequestParam(required = false)
            String name,

            @RequestParam(required = false)
            BigDecimal minPrice,

            @RequestParam(required = false)
            BigDecimal maxPrice,

            @RequestParam(required = false)
            Boolean active,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        ProductFilterRequest productFilter = new ProductFilterRequest();

        productFilter.setNombre(name);
        productFilter.setPrecioMin(minPrice);
        productFilter.setPrecioMax(maxPrice);
        productFilter.setActivo(active);
        productFilter.setPage(page);
        productFilter.setSize(size);

        Page<ProductResponse> response =
                service.execute(productFilter)
                        .map(mapper::toResponse);

        return ResponseEntity.ok(response);
    }
}
