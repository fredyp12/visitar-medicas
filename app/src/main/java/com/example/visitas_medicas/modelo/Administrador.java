package com.example.visitas_medicas.modelo;

public class Administrador {
    public Administrador () {

    }

    private int id;
    private String nombre;
    private String contraseña;
    private String correo;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return contraseña;
    }

    public void setClave(String clave) {
        this.contraseña = clave;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
