package com.unicid.sca_eventos_android;

public class Inscricao {
    private String id;
    private Evento evento;
    private String dataInscricao;
    private boolean presencaConfirmada;

    public String getId() { return id; }
    public Evento getEvento() { return evento; }
    public String getDataInscricao() { return dataInscricao; }
    public boolean isPresencaConfirmada() { return presencaConfirmada; }
}