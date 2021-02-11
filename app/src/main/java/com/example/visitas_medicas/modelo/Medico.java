package com.example.visitas_medicas.modelo;

public class Medico {

    public Medico () {

    }

    private String id;
    private String nombre;
    private String contraseña;
    private String especialidad;
    private String pregunta;
    private String respuesta;

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

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String clave) {
        this.contraseña = clave;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String oregunta) {
        this.pregunta = oregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

}
