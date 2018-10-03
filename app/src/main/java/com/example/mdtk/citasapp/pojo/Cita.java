package com.example.mdtk.citasapp.pojo;

import com.example.mdtk.citasapp.constantes.G;

public class Cita {
    private int ID;
    private String servicio;
    private String cliente;
    private String nota;
    private String fechaHora;
    private int empleadoID;

    public Cita(int ID, String servicio, String cliente, String nota, String fechaHora,int empleadoID) {
        this.ID = ID;
        this.servicio = servicio;
        this.cliente = cliente;
        this.nota = nota;
        this.fechaHora = fechaHora;
        this.empleadoID = empleadoID;
    }

    public Cita() { }

    public int getEmpleadoID() {
        return empleadoID;
    }

    public void setEmpleadoID(int empleadoID) {
        this.empleadoID = empleadoID;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}
