package com.unicid.sca_eventos_android.api;

import com.google.gson.JsonObject;
import com.unicid.sca_eventos_android.models.Evento;
import com.unicid.sca_eventos_android.models.Inscricao;
import com.unicid.sca_eventos_android.models.LoginResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Interface que define os endpoints da API para Retrofit.
 */
public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body JsonObject loginRequest);

    @POST("auth/registrar")
    Call<Void> registrar(@Body JsonObject registroRequest);

    @POST("scanner/validar")
    Call<String> validarAcesso(@Body JsonObject registroRequest);

    @GET("eventos")
    Call<List<Evento>> listarEventos();

    @GET("inscricoes/aluno/{alunoId}")
    Call<List<Inscricao>> listarInscricoes(@Path("alunoId") String alunoId);
}