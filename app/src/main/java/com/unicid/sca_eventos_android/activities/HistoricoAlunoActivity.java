package com.unicid.sca_eventos_android.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.unicid.sca_eventos_android.R;
import com.unicid.sca_eventos_android.adapters.HistoricoAdapter;
import com.unicid.sca_eventos_android.api.ApiClient;
import com.unicid.sca_eventos_android.api.ApiService;
import com.unicid.sca_eventos_android.models.Inscricao;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Exibe o histórico de eventos em que o aluno confirmou presença.
 */
public class HistoricoAlunoActivity extends AppCompatActivity {

    private RecyclerView rvHistorico;
    private ImageButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_aluno);

        rvHistorico = findViewById(R.id.rvHistorico);
        btnVoltar = findViewById(R.id.btnVoltarHistorico);

        rvHistorico.setLayoutManager(new LinearLayoutManager(this));

        btnVoltar.setOnClickListener(v -> finish());

        carregarHistorico();
    }

    private void carregarHistorico() {
        SharedPreferences prefs = getSharedPreferences("SCA_PREFS", MODE_PRIVATE);
        String alunoId = prefs.getString("USER_ID", "");

        if (alunoId.isEmpty()) {
            Toast.makeText(this, "Erro: Usuário não identificado", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.listarHistoricoAluno(alunoId).enqueue(new Callback<List<Inscricao>>() {
            @Override
            public void onResponse(Call<List<Inscricao>> call, Response<List<Inscricao>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Inscricao> lista = response.body();
                    if (lista.isEmpty()) {
                        Toast.makeText(HistoricoAlunoActivity.this, "Você ainda não tem inscrições", Toast.LENGTH_SHORT).show();
                    }
                    HistoricoAdapter adapter = new HistoricoAdapter(lista);
                    rvHistorico.setAdapter(adapter);
                } else {
                    Toast.makeText(HistoricoAlunoActivity.this, "Erro ao carregar histórico", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Inscricao>> call, Throwable t) {
                Toast.makeText(HistoricoAlunoActivity.this, "Erro de rede: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}