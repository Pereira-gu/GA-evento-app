package com.unicid.sca_eventos_android;

public class LoginResponse {
    private String id;
    private String nome;
    private String perfil;
    private boolean badgeOuro; // Campo sincronizado com o Backend [cite: 163]

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getPerfil() { return perfil; }
    public boolean isBadgeOuro() { return badgeOuro; }
}