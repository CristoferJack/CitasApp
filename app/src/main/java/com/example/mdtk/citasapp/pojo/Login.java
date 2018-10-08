package com.example.mdtk.citasapp.pojo;

public class Login {
    private int ID;
    private int id_trabajador_registro;
    private int estado;

    public Login() {
    }

    public Login(int ID, int id_trabajador_registro, int estado) {
        this.ID = ID;
        this.id_trabajador_registro = id_trabajador_registro;
        this.estado = estado;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getId_trabajador_registro() {
        return id_trabajador_registro;
    }

    public void setId_trabajador_registro(int id_trabajador_registro) {
        this.id_trabajador_registro = id_trabajador_registro;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
