package com.digitalMarket.iamService.presentation.controllers;


import com.digitalMarket.iamService.application.dto.request.UsuariosRequest;
import com.digitalMarket.iamService.application.dto.response.UsuariosLoadResponse;
import com.digitalMarket.iamService.application.dto.response.UsuariosResponse;
import com.digitalMarket.iamService.application.service.UsuariosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
public class UsuariosController {

    @Autowired
    private final UsuariosService usuariosService;

    public UsuariosController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }


    @PostMapping
    public ResponseEntity<UsuariosResponse> post(@Valid @RequestBody UsuariosRequest request) {
        UsuariosResponse response = usuariosService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path="{id}")
    public ResponseEntity<UsuariosLoadResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(usuariosService.readWithRoles(id));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<UsuariosResponse> put(@PathVariable Long id, @Valid @RequestBody UsuariosRequest request) {
        return ResponseEntity.ok(usuariosService.update(request, id));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<UsuariosResponse> delete(@Valid @PathVariable Long id) {
        this.usuariosService.delete(id);
        return ResponseEntity.noContent().build();
    }
}