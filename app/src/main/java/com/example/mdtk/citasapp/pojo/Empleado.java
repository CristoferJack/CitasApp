package com.example.mdtk.citasapp.pojo;

public class Empleado {
    private int ID;
    private String nombres;

    public Empleado() {
    }

    public Empleado(int ID, String nombres) {
        this.ID = ID;
        this.nombres = nombres;
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
