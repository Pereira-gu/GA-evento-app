package com.unicid.sca_eventos_android.api;

import com.google.gson.JsonObject;
import com.unicid.sca_eventos_android.models.Evento;
import com.unicid.sca_eventos_android.models.Inscricao;
import com.unicid.sca_eventos_android.models.LoginResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Interface que define os endpoints da API para Retrofit.
 * Sincronizado com a documentação do backend.
 */
public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body JsonObject loginRequest);

    @POST("auth/registrar")
    Call<Void> registrar(@Body JsonObject registroRequest);

    @POST("scanner/validar")
    Call<String> validarAcesso(@Body JsonObject validarRequest);

    @GET("api/eventos")
    Call<List<Evento>> listarEventos();

    @POST("api/eventos")
    Call<Evento> criarEvento(@Body Evento evento);

    @PUT("api/eventos/{id}")
    Call<Evento> atualizarEvento(@Path("id") String id, @Body Evento evento);

    @DELETE("api/eventos/{id}")
    Call<Void> deletarEvento(@Path("id") String id);

    @GET("api/historico/aluno/{alunoId}")
    Call<List<Inscricao>> listarInscricoes(@Path("alunoId") String alunoId);

    // Novas rotas de Inscrição
    @POST("api/inscricoes")
    Call<Void> inscreverEmEvento(@Body JsonObject inscricaoRequest);

    @GET("api/inscricoes/aluno/{alunoId}")
    Call<List<Inscricao>> listarHistoricoAluno(@Path("alunoId") String alunoId);

    @GET("api/inscricoes/evento/{eventoId}/inscritos")
    Call<List<JsonObject>> listarInscritosNoEvento(@Path("eventoId") String eventoId);
}
