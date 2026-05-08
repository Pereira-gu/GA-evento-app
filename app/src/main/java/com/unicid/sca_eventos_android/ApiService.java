package com.unicid.sca_eventos_android;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body JsonObject loginRequest);

    @POST("auth/registrar")
    Call<Void> registrar(@Body JsonObject registroRequest);

    @POST("scanner/validar")
    Call<String> validarAcesso(@Body JsonObject registroRequest);

    @retrofit2.http.GET("eventos")
    Call<java.util.List<Evento>> listarEventos();

    @retrofit2.http.GET("inscricoes/aluno/{alunoId}")
    Call<java.util.List<Inscricao>> listarInscricoes(@retrofit2.http.Path("alunoId") String alunoId);
}