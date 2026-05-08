package com.unicid.sca_eventos_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
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

public class SelecionarEventoInscritosActivity extends AppCompatActivity {

    private RecyclerView rvEventos;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_evento_inscritos);

        rvEventos = findViewById(R.id.rvEventosInscritos);
        ImageButton btnVoltar = findViewById(R.id.btnVoltarSelecao);

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
                        Intent intent = new Intent(SelecionarEventoInscritosActivity.this, ListaInscritosActivity.class);
                        intent.putExtra("EVENTO_ID", evento.getId());
                        intent.putExtra("EVENTO_TITULO", evento.getTitulo());
                        startActivity(intent);
                    });
                    rvEventos.setAdapter(adapter);
                } else {
                    Toast.makeText(SelecionarEventoInscritosActivity.this, "Erro ao carregar eventos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Toast.makeText(SelecionarEventoInscritosActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }
}