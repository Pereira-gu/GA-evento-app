package com.unicid.sca_eventos_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelecaoEventoActivity extends AppCompatActivity {

    private RecyclerView rvEventos;
    private Button btnVoltarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecao_evento);

        rvEventos = findViewById(R.id.rvEventos);
        btnVoltarLogin = findViewById(R.id.btnVoltarLogin);

        rvEventos.setLayoutManager(new LinearLayoutManager(this));

        carregarEventos();

        btnVoltarLogin.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("SCA_PREFS", MODE_PRIVATE);
            prefs.edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void carregarEventos() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.listarEventos().enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    EventoAdapter adapter = new EventoAdapter(response.body(), evento -> {
                        Intent intent = new Intent(SelecaoEventoActivity.this, ScannerPorteiroActivity.class);
                        intent.putExtra("EVENTO_ID", evento.getId());
                        intent.putExtra("EVENTO_NOME", evento.getNome());
                        startActivity(intent);
                    });
                    rvEventos.setAdapter(adapter);
                } else {
                    Toast.makeText(SelecaoEventoActivity.this, "Erro ao carregar eventos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Toast.makeText(SelecaoEventoActivity.this, "Erro de rede: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}