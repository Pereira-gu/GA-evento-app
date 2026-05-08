package com.unicid.sca_eventos_android.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.unicid.sca_eventos_android.R;
import com.unicid.sca_eventos_android.adapters.EventoAdapter;
import com.unicid.sca_eventos_android.api.ApiClient;
import com.unicid.sca_eventos_android.api.ApiService;
import com.unicid.sca_eventos_android.models.Evento;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity para o Organizador gerenciar (CRUD) os eventos do sistema.
 */
public class GerenciarEventosActivity extends AppCompatActivity {

    private RecyclerView rvEventos;
    private EventoAdapter adapter;
    private List<Evento> listaEventos = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_eventos);

        rvEventos = findViewById(R.id.rvGerenciarEventos);
        ImageButton btnVoltar = findViewById(R.id.btnVoltarGerenciar);
        FloatingActionButton fabAdd = findViewById(R.id.fabAddEvento);

        apiService = ApiClient.getClient().create(ApiService.class);
        rvEventos.setLayoutManager(new LinearLayoutManager(this));
        
        // Inicialmente usamos o EventoAdapter existente, mas podemos precisar de um específico 
        // para incluir botões de editar/excluir.
        adapter = new EventoAdapter(listaEventos);
        rvEventos.setAdapter(adapter);

        btnVoltar.setOnClickListener(v -> finish());

        fabAdd.setOnClickListener(v -> mostrarDialogoEvento(null));

        carregarEventos();
    }

    private void carregarEventos() {
        apiService.listarEventos().enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaEventos.clear();
                    listaEventos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Toast.makeText(GerenciarEventosActivity.this, "Erro ao carregar eventos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoEvento(Evento eventoExistente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_evento, null);
        
        EditText etNome = view.findViewById(R.id.etNomeEventoDialog);
        EditText etData = view.findViewById(R.id.etDataEventoDialog);
        
        if (eventoExistente != null) {
            etNome.setText(eventoExistente.getNome());
            etData.setText(eventoExistente.getData());
            builder.setTitle("Editar Evento");
        } else {
            builder.setTitle("Novo Evento");
        }

        builder.setView(view);
        builder.setPositiveButton("Salvar", (dialog, which) -> {
            String nome = etNome.getText().toString();
            String data = etData.getText().toString();
            
            if (eventoExistente == null) {
                salvarNovoEvento(new Evento(null, nome, data));
            } else {
                atualizarEvento(eventoExistente.getId(), new Evento(eventoExistente.getId(), nome, data));
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void salvarNovoEvento(Evento novo) {
        apiService.criarEvento(novo).enqueue(new Callback<Evento>() {
            @Override
            public void onResponse(Call<Evento> call, Response<Evento> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GerenciarEventosActivity.this, "Evento criado!", Toast.LENGTH_SHORT).show();
                    carregarEventos();
                }
            }

            @Override
            public void onFailure(Call<Evento> call, Throwable t) {
                Toast.makeText(GerenciarEventosActivity.this, "Erro ao criar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void atualizarEvento(String id, Evento atualizado) {
        apiService.atualizarEvento(id, atualizado).enqueue(new Callback<Evento>() {
            @Override
            public void onResponse(Call<Evento> call, Response<Evento> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GerenciarEventosActivity.this, "Evento atualizado!", Toast.LENGTH_SHORT).show();
                    carregarEventos();
                }
            }

            @Override
            public void onFailure(Call<Evento> call, Throwable t) {
                Toast.makeText(GerenciarEventosActivity.this, "Erro ao atualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}