package com.example.visitas_medicas.modelo;

public class Consultorio {
    private String id;
    private String nombre;

    public Consultorio(String id) {
        this.id = id;
    }

    public Consultorio(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
