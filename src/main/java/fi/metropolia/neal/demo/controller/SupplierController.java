package fi.metropolia.neal.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.metropolia.neal.demo.service.SupplierService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import fi.metropolia.neal.demo.dto.SupplierDTO;
import fi.metropolia.neal.demo.entity.Supplier;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getSupplier(@PathVariable int id) {
        return supplierService.getSupplierById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SupplierDTO create(@RequestBody SupplierDTO dto) {
        return toDTO(supplierService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> update(@PathVariable int id, @RequestBody SupplierDTO dto) {
        return supplierService.update(id, dto)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private SupplierDTO toDTO(Supplier s) {
        return new SupplierDTO(
                s.getId(),
                s.getName(),
                s.getContactName(),
                s.getPhone(),
                s.getEmail(),
                s.getProducts().stream().map(p -> p.getId()).toList()
        );
    }
}

