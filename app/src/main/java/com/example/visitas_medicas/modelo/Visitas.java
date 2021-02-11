package com.example.visitas_medicas.modelo;

public class Visitas {
    private String id;
    private String paciente;
    private String estado;
    private String fecha;
    private String hora;
    private String doctor;
    private String especialidad;
    private String consultorio;

    public Visitas() {

    }

    public Visitas(String id, String paciente, String estado, String fecha, String doctor, String especialidad, String hora) {
        this.id = id;
        this.paciente = paciente;
        this.estado = estado;
        this.fecha = fecha;
        this.doctor=doctor;
        this.especialidad=especialidad;
        this.hora=hora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(String consultorio) {
        this.consultorio = consultorio;
    }
}
