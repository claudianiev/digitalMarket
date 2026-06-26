package com.digitalMarket.iamService.presentation.controllers;

import com.digitalMarket.iamService.application.dto.request.PermisosRequest;
import com.digitalMarket.iamService.application.dto.response.PermisosResponse;
import com.digitalMarket.iamService.application.service.PermisosService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "permits")
@AllArgsConstructor
public class PermisosController {

    private final PermisosService permisosService;

    @PostMapping
    public ResponseEntity<PermisosResponse> post(@Valid  @RequestBody PermisosRequest request) {
        return ResponseEntity.ok(permisosService.create(request));
    }

    @GetMapping(path="{id}")
    public ResponseEntity<PermisosResponse> get(@Valid @PathVariable Long id) {
        System.out.println(id);
       return ResponseEntity.ok(permisosService.read(id));
    }

    @PatchMapping(path="/readByIdRol/{id}")
    public ResponseEntity<List<PermisosResponse>> readByIdRol(@Valid @PathVariable Long id) {
        var  response = this.permisosService.readByIdRol(id);
        return ResponseEntity.ok((List<PermisosResponse>) response);
    }


    @PutMapping(path = "{id}")
    public ResponseEntity<PermisosResponse> put(@Valid @PathVariable Long id, @RequestBody PermisosRequest request) {
        return ResponseEntity.ok(this.permisosService.update(request, id));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<PermisosResponse> delete(@Valid @PathVariable Long id) {
        this.permisosService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
