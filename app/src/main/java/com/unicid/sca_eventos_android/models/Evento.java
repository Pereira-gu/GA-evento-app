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