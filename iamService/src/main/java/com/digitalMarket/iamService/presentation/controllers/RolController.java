package com.digitalMarket.iamService.presentation.controllers;

import com.digitalMarket.iamService.application.dto.request.RolRequest;
import com.digitalMarket.iamService.application.dto.response.RolResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.digitalMarket.iamService.application.service.RolService;



@RestController
@RequestMapping(path = "role")
@AllArgsConstructor
public class RolController {

    private final RolService rolService;

    @PostMapping
    public ResponseEntity<RolResponse> post(@Valid @RequestBody RolRequest request) {
        System.out.println(request);
        return ResponseEntity.ok(rolService.create(request));
    }

    @GetMapping(path="{id}")
    public ResponseEntity<RolResponse> get(@Valid @PathVariable Integer id) {
        System.out.println(id);
        return ResponseEntity.ok(rolService.read(id));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<RolResponse> put(@Valid @PathVariable Integer id, @RequestBody RolRequest request) {
        return ResponseEntity.ok(this.rolService.update(request, id));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<RolResponse> delete(@Valid @PathVariable Integer id) {
        this.rolService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
