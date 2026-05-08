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
                    EventoAdapter adapter = new EventoAdapter(response.body(), evento -> {
                        mostrarDetalhesEvento(evento);
                    });
                    rvEventos.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Toast.makeText(ListaEventosAlunoActivity.this, "Erro ao carregar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDetalhesEvento(Evento evento) {
        new AlertDialog.Builder(this)
            .setTitle(evento.getTitulo())
            .setMessage("Local: " + evento.getLocal() + "\nData: " + evento.getDataInicio() + "\nCarga Horária: " + evento.getCargaHoraria() + "h")
            .setPositiveButton("OK", null)
            .show();
    }
}