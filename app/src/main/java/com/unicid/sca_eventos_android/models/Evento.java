package com.unicid.sca_eventos_android.models;

/**
 * Modelo que representa um Evento no sistema.
 * Ajustado para os campos do Backend: titulo, local, dataInicio, cargaHoraria.
 */
public class Evento {
    private String id;
    private String titulo;
    private String local;
    private String dataInicio;
    private int cargaHoraria;

    public Evento(String id, String titulo, String local, String dataInicio, int cargaHoraria) {
        this.id = id;
        this.titulo = titulo;
        this.local = local;
        this.dataInicio = dataInicio;
        this.cargaHoraria = cargaHoraria;
    }

    // Construtor para novos eventos (sem ID)
    public Evento(String titulo, String local, String dataInicio, int cargaHoraria) {
        this.titulo = titulo;
        this.local = local;
        this.dataInicio = dataInicio;
        this.cargaHoraria = cargaHoraria;
    }

    public void setId(String id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setLocal(String local) { this.local = local; }
    public void setDataInicio(String dataInicio) { this.dataInicio = dataInicio; }
    public void setCargaHoraria(int cargaHoraria) { this.cargaHoraria = cargaHoraria; }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getLocal() { return local; }
    public String getDataInicio() { return dataInicio; }
    public int getCargaHoraria() { return cargaHoraria; }

    @Override
    public String toString() {
        return titulo;
    }
}