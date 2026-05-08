package com.unicid.sca_eventos_android.models;

/**
 * Objeto de resposta da API para operações de login.
 * Contém dados do perfil e identificação do usuário.
 */
public class LoginResponse {
    private String id;
    private String nome;
    private String perfil;
    private boolean badgeOuro;

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getPerfil() { return perfil; }
    public boolean isBadgeOuro() { return badgeOuro; }
}