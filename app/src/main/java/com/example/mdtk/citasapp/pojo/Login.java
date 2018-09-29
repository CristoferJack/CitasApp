package com.example.mdtk.citasapp.pojo;

public class Login {
    private int ID;
    private int empleadoID;
    private int estado;

    public Login() {
    }

    public Login(int ID, int empleadoID, int estado) {
        this.ID = ID;
        this.empleadoID = empleadoID;
        this.estado = estado;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getEmpleadoID() {
        return empleadoID;
    }

    public void setEmpleadoID(int empleadoID) {
        this.empleadoID = empleadoID;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
