package com.unicid.sca_eventos_android;

public class Evento {
    private String id;
    private String nome;
    private String data;

    public Evento(String id, String nome, String data) {
        this.id = id;
        this.nome = nome;
        this.data = data;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getData() { return data; }

    @Override
    public String toString() {
        return nome; // Útil para simplificar o adaptador se usarmos um Spinner
    }
}