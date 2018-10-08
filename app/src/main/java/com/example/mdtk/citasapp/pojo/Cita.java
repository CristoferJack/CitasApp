package com.example.mdtk.citasapp.pojo;

public class Cita {
    private int ID;
    private String servicio;
    private String cliente;
    private String nota;
    private String fechaHora;
    private int id_trabajador;
    private int id_trabajador_registro;
    private int estado;

    public Cita() { }

    public Cita(int ID, String servicio, String cliente, String nota, String fechaHora,
                int id_trabajador, int id_trabajador_registro, int estado) {
        this.ID = ID;
        this.servicio = servicio;
        this.cliente = cliente;
        this.nota = nota;
        this.fechaHora = fechaHora;
        this.id_trabajador = id_trabajador;
        this.id_trabajador_registro = id_trabajador_registro;
        this.estado = estado;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getId_trabajador_registro() {
        return id_trabajador_registro;
    }

    public void setId_trabajador_registro(int id_trabajador_registro) {
        this.id_trabajador_registro = id_trabajador_registro;
    }

    public int getId_trabajador() {
        return id_trabajador;
    }

    public void setId_trabajador(int id_trabajador) {
        this.id_trabajador = id_trabajador;
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
