package com.example.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MessageRequestDTO {
    @NotBlank(message = "nombre is required")
    @Size(min = 1, max = 1000, message = "Content must be between 1 and 1000 characters")
    private String nombre;

    @NotBlank(message = "descripcion is required")
    @Size(min = 2, max = 100, message = "Source must be between 2 and 100 characters")
    private String descripcion;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}