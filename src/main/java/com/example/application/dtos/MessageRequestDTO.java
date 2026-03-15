package com.example.application.dtos;

import com.example.api.model.ListTask;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class MessageRequestDTO {
    @NotBlank(message = "nombre is required")
    @Size(min = 1, max = 1000, message = "Content must be between 1 and 1000 characters")
    private String nombre;

    @NotBlank(message = "descripcion is required")
    @Size(min = 2, max = 100, message = "Source must be between 2 and 100 characters")
    private String descripcion;
    private String telephone;
    private String email;
    private String publisher;
    private String version;
    private String created_at;
    private String updated_at;
    List<ListTask> tasks;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

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
    public List<ListTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<ListTask> tasks) {
        this.tasks = tasks;
    }
}