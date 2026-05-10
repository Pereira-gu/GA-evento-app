package com.unicid.sca_eventos_android.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.appcompat.app.AlertDialog;

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
        
        adapter = new EventoAdapter(listaEventos, 
            evento -> mostrarDialogoEvento(evento), // Clique simples: Editar
            evento -> confirmarExclusao(evento)     // Clique longo: Excluir
        );
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

        EditText etTitulo = view.findViewById(R.id.etTituloEventoDialog);
        EditText etLocal = view.findViewById(R.id.etLocalEventoDialog);
        EditText etDataInicio = view.findViewById(R.id.etDataEventoDialog);
        EditText etCarga = view.findViewById(R.id.etCargaHorariaDialog);

        // Desativar teclado para o campo de data e mostrar DatePicker
        etDataInicio.setFocusable(false);
        etDataInicio.setClickable(true);
        etDataInicio.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
                new TimePickerDialog(this, (view2, hourOfDay, minute) -> {
                    String dataFormatada = String.format(Locale.getDefault(), "%04d-%02d-%02dT%02d:%02d:00",
                            year, month + 1, dayOfMonth, hourOfDay, minute);
                    etDataInicio.setText(dataFormatada);
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        if (eventoExistente != null) {
            etTitulo.setText(eventoExistente.getTitulo());
            etLocal.setText(eventoExistente.getLocal());
            etDataInicio.setText(eventoExistente.getDataInicio());
            etCarga.setText(String.valueOf(eventoExistente.getCargaHoraria()));
            builder.setTitle("Editar Evento");
        } else {
            builder.setTitle("Novo Evento");
        }

        builder.setView(view);
        builder.setPositiveButton("Salvar", (dialog, which) -> {
            String titulo = etTitulo.getText().toString();
            String local = etLocal.getText().toString();
            String dataInicio = etDataInicio.getText().toString();
            String cargaStr = etCarga.getText().toString();
            int cargaHoraria = Integer.parseInt(cargaStr.isEmpty() ? "0" : cargaStr);

            if (eventoExistente == null) {
                salvarNovoEvento(new Evento(null, titulo, local, dataInicio, cargaHoraria));
            } else {
                atualizarEvento(eventoExistente.getId(), new Evento(eventoExistente.getId(), titulo, local, dataInicio, cargaHoraria));
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

    private void confirmarExclusao(Evento evento) {
        new AlertDialog.Builder(this)
            .setTitle("Excluir Evento")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage("Deseja realmente excluir o evento '" + evento.getTitulo() + "'?\n\nEsta ação não pode ser desfeita.")
            .setPositiveButton("Excluir", (dialog, which) -> {
                apiService.deletarEvento(evento.getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(GerenciarEventosActivity.this, "Evento excluído com sucesso", Toast.LENGTH_SHORT).show();
                            carregarEventos();
                        } else {
                            Toast.makeText(GerenciarEventosActivity.this, "Erro ao excluir: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(GerenciarEventosActivity.this, "Erro de rede ao excluir", Toast.LENGTH_SHORT).show();
                    }
                });
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }
}