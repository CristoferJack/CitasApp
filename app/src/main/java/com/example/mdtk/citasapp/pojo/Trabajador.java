package com.example.mdtk.citasapp.pojo;

public class Trabajador {
    private int ID;
    private String nombres;
    private String telefono;

    public Trabajador() {
    }

    public Trabajador(int ID, String nombres, String telefono) {
        this.ID = ID;
        this.nombres = nombres;
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    @Override
    public String toString() {
        return nombres;
    }

}
