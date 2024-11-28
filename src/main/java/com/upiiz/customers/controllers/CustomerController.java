package com.upiiz.customers.controllers;

import com.upiiz.customers.entities.Customer;
import com.upiiz.customers.repository.CustomerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Gestión de clientes", description = "Endpoints para gestionar la información del cliente")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MODERATOR', 'DEVELOPER', 'ANALYST')")
    @Operation(summary = "Lista de todos los clientes", description = "Recuperar una lista de todos los clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes recuperados exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso no autorizado")
    })
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @Operation(summary = "Crear un nuevo cliente", description = "Agregar un nuevo cliente al sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente creado con éxito"),
            @ApiResponse(responseCode = "403", description = "Acceso no autorizado")
    })
    public Customer createCustomer(
            @Parameter(description = "Datos del cliente", required = true)
            @RequestBody Customer customer
    ) {
        return customerRepository.save(customer);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'DEVELOPER', 'EDITOR')")
    @Operation(summary = "Actualizar un cliente", description = "Actualiza los datos de un cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso no autorizado")
    })
    public ResponseEntity<Customer> updateCustomer(
            @Parameter(description = "Cliente ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Actualiar los datos de un cliente", required = true)
            @RequestBody Customer customerDetails
    ) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        customer.setFirstName(customerDetails.getFirstName());
        customer.setLastName(customerDetails.getLastName());
        customer.setEmail(customerDetails.getEmail());
        customer.setPhoneNumber(customerDetails.getPhoneNumber());
        return ResponseEntity.ok(customerRepository.save(customer));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER', 'ANALYST')")
    @Operation(summary = "Eliminar un cliente", description = "Remover un cliente del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso no autorizado")
    })
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "ID de cliente a eliminar", required = true)
            @PathVariable Long id
    ) {
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}