package com.example.visitas_medicas.modelo;

public class Historial_clinico {
    private String id;
    private String fecha;
    private String paciente;
    private String medico;
    private String valoracion;

    public Historial_clinico() {
    }

    public Historial_clinico(String id, String fecha, String paciente, String medico, String valoracion) {
        this.id = id;
        this.fecha = fecha;
        this.paciente = paciente;
        this.medico = medico;
        this.valoracion = valoracion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getValoracion() {
        return valoracion;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }
}
