package com.example.mdtk.citasapp.pojo;

public class SincronizacionRegistro {
    private int ID;
    private String id_cita;
    private int operacion;
    private int id_trabajador_registro;

    public SincronizacionRegistro() {
    }

    public SincronizacionRegistro(int ID, String id_cita, int operacion, int id_trabajador_registro) {
        this.ID = ID;
        this.id_cita = id_cita;
        this.operacion = operacion;
        this.id_trabajador_registro = id_trabajador_registro;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getId_cita() {
        return id_cita;
    }

    public void setId_cita(String id_cita) {
        this.id_cita = id_cita;
    }

    public int getOperacion() {
        return operacion;
    }

    public void setOperacion(int operacion) {
        this.operacion = operacion;
    }

    public int getId_trabajador_registro() {
        return id_trabajador_registro;
    }

    public void setId_trabajador_registro(int id_trabajador_registro) {
        this.id_trabajador_registro = id_trabajador_registro;
    }
}
