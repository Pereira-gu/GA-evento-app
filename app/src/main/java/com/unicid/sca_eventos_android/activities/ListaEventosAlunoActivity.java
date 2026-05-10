package com.unicid.sca_eventos_android.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.unicid.sca_eventos_android.R;
import com.unicid.sca_eventos_android.adapters.EventoAdapter;
import com.unicid.sca_eventos_android.api.ApiClient;
import com.unicid.sca_eventos_android.api.ApiService;
import com.unicid.sca_eventos_android.models.Evento;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Lista eventos para o aluno visualizar.
 */
public class ListaEventosAlunoActivity extends AppCompatActivity {

    private RecyclerView rvEventos;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eventos_aluno);

        rvEventos = findViewById(R.id.rvListaEventosAluno);
        ImageButton btnVoltar = findViewById(R.id.btnVoltarLista);

        apiService = ApiClient.getClient().create(ApiService.class);
        rvEventos.setLayoutManager(new LinearLayoutManager(this));

        btnVoltar.setOnClickListener(v -> finish());

        carregarEventos();
    }

    private void carregarEventos() {
        apiService.listarEventos().enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Evento> eventos = response.body();
                    if (eventos.isEmpty()) {
                        Toast.makeText(ListaEventosAlunoActivity.this, R.string.msg_no_events, Toast.LENGTH_LONG).show();
                    }
                    EventoAdapter adapter = new EventoAdapter(eventos, evento -> {
                        mostrarDetalhesEvento(evento);
                    });
                    rvEventos.setAdapter(adapter);
                } else {
                    Toast.makeText(ListaEventosAlunoActivity.this, R.string.error_load_events, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Toast.makeText(ListaEventosAlunoActivity.this, R.string.error_network, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDetalhesEvento(Evento evento) {
        // Formatar Data para exibição amigável
        String dataFormatada = evento.getDataInicio();
        try {
            java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault());
            String dataStr = evento.getDataInicio();
            if (dataStr.contains(".")) dataStr = dataStr.split("\\.")[0];
            java.util.Date date = inputFormat.parse(dataStr);
            java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", new java.util.Locale("pt", "BR"));
            dataFormatada = outputFormat.format(date);
        } catch (Exception ignored) {}

        // Formatar Carga Horária
        int totalMinutos = evento.getCargaHoraria();
        int horas = totalMinutos / 60;
        int minutos = totalMinutos % 60;
        String cargaStr = (horas > 0 ? horas + "h " : "") + (minutos > 0 ? minutos + "m" : "");
        if (cargaStr.isEmpty()) cargaStr = "0m";

        new AlertDialog.Builder(this)
            .setTitle(evento.getTitulo())
            .setMessage(getString(R.string.label_local, evento.getLocal()) + 
                        "\n" + getString(R.string.label_date, dataFormatada) + 
                        "\n" + getString(R.string.label_workload, cargaStr))
            .setPositiveButton(R.string.action_close, null)
            .setNeutralButton(R.string.action_subscribe, (dialog, which) -> {
                efetuarInscricao(evento.getId());
            })
            .show();
    }

    private void efetuarInscricao(String eventoId) {
        android.content.SharedPreferences prefs = getSharedPreferences("SCA_PREFS", MODE_PRIVATE);
        String usuarioId = prefs.getString("USER_ID", null);

        if (usuarioId == null) {
            Toast.makeText(this, R.string.error_user_not_found, Toast.LENGTH_SHORT).show();
            return;
        }

        com.google.gson.JsonObject json = new com.google.gson.JsonObject();
        json.addProperty("usuarioId", usuarioId);
        json.addProperty("eventoId", eventoId);

        apiService.inscreverEmEvento(json).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ListaEventosAlunoActivity.this, R.string.msg_inscription_success, Toast.LENGTH_LONG).show();
                } else if (response.code() == 409) {
                    Toast.makeText(ListaEventosAlunoActivity.this, R.string.msg_already_inscribed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ListaEventosAlunoActivity.this, getString(R.string.error_inscription, response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ListaEventosAlunoActivity.this, R.string.error_network, Toast.LENGTH_SHORT).show();
            }
        });
    }
}