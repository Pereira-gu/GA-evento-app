package com.unicid.sca_eventos_android.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.JsonObject;
import com.unicid.sca_eventos_android.R;
import com.unicid.sca_eventos_android.adapters.InscritoAdapter;
import com.unicid.sca_eventos_android.api.ApiClient;
import com.unicid.sca_eventos_android.api.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaInscritosActivity extends AppCompatActivity {

    private RecyclerView rvInscritos;
    private TextView tvTitulo;
    private String eventoId;
    private String tituloEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_inscritos);

        eventoId = getIntent().getStringExtra("EVENTO_ID");
        tituloEvento = getIntent().getStringExtra("EVENTO_TITULO");

        rvInscritos = findViewById(R.id.rvInscritos);
        tvTitulo = findViewById(R.id.tvTituloEventoInscritos);
        ImageButton btnVoltar = findViewById(R.id.btnVoltarInscritos);

        if (tituloEvento != null) {
            tvTitulo.setText("Inscritos: " + tituloEvento);
        }

        rvInscritos.setLayoutManager(new LinearLayoutManager(this));
        btnVoltar.setOnClickListener(v -> finish());

        carregarInscritos();
    }

    private void carregarInscritos() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.listarInscritosNoEvento(eventoId).enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    InscritoAdapter adapter = new InscritoAdapter(response.body());
                    rvInscritos.setAdapter(adapter);
                } else {
                    Toast.makeText(ListaInscritosActivity.this, "Erro ao carregar inscritos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {
                Toast.makeText(ListaInscritosActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }
}