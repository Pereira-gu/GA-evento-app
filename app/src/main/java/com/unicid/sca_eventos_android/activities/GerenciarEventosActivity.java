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
                } else {
                    Toast.makeText(GerenciarEventosActivity.this, R.string.error_load_events, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Toast.makeText(GerenciarEventosActivity.this, R.string.error_network, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoEvento(Evento eventoExistente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_evento, null);

        com.google.android.material.textfield.TextInputLayout tilTitulo = view.findViewById(R.id.tilTitulo);
        com.google.android.material.textfield.TextInputLayout tilLocal = view.findViewById(R.id.tilLocal);
        com.google.android.material.textfield.TextInputLayout tilData = view.findViewById(R.id.tilData);
        com.google.android.material.textfield.TextInputLayout tilCarga = view.findViewById(R.id.tilCarga);

        EditText etTitulo = view.findViewById(R.id.etTituloEventoDialog);
        EditText etLocal = view.findViewById(R.id.etLocalEventoDialog);
        EditText etDataInicio = view.findViewById(R.id.etDataEventoDialog);
        EditText etCarga = view.findViewById(R.id.etCargaHorariaDialog);

        etDataInicio.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
                new TimePickerDialog(this, (view2, hourOfDay, minute) -> {
                    String dataFormatada = String.format(Locale.getDefault(), "%04d-%02d-%02dT%02d:%02d:00",
                            year, month + 1, dayOfMonth, hourOfDay, minute);
                    etDataInicio.setText(dataFormatada);
                    tilData.setError(null);
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        if (eventoExistente != null) {
            etTitulo.setText(eventoExistente.getTitulo());
            etLocal.setText(eventoExistente.getLocal());
            etDataInicio.setText(eventoExistente.getDataInicio());
            etCarga.setText(String.valueOf(eventoExistente.getCargaHoraria()));
            builder.setTitle(R.string.title_edit_event);
        } else {
            builder.setTitle(R.string.title_new_event);
        }

        builder.setView(view);
        builder.setPositiveButton(R.string.action_save, null);
        builder.setNegativeButton(R.string.action_cancel, null);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String titulo = etTitulo.getText().toString().trim();
            String local = etLocal.getText().toString().trim();
            String dataInicio = etDataInicio.getText().toString().trim();
            String cargaStr = etCarga.getText().toString().trim();

            boolean isValid = true;
            if (titulo.isEmpty()) { tilTitulo.setError(getString(R.string.msg_error_required)); isValid = false; } else { tilTitulo.setError(null); }
            if (local.isEmpty()) { tilLocal.setError(getString(R.string.msg_error_required)); isValid = false; } else { tilLocal.setError(null); }
            if (dataInicio.isEmpty()) { tilData.setError(getString(R.string.msg_error_required)); isValid = false; } else { tilData.setError(null); }
            if (cargaStr.isEmpty()) { tilCarga.setError(getString(R.string.msg_error_required)); isValid = false; } else { tilCarga.setError(null); }

            if (isValid) {
                int cargaHoraria = Integer.parseInt(cargaStr);
                if (eventoExistente == null) {
                    // Usa o novo construtor sem ID para evitar enviar "id": null
                    salvarNovoEvento(new Evento(titulo, local, dataInicio, cargaHoraria));
                } else {
                    atualizarEvento(eventoExistente.getId(), new Evento(eventoExistente.getId(), titulo, local, dataInicio, cargaHoraria));
                }
                dialog.dismiss();
            }
        });
    }

    private void salvarNovoEvento(Evento novo) {
        apiService.criarEvento(novo).enqueue(new Callback<Evento>() {
            @Override
            public void onResponse(Call<Evento> call, Response<Evento> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GerenciarEventosActivity.this, R.string.msg_event_created, Toast.LENGTH_SHORT).show();
                    carregarEventos();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Sem detalhes";
                        android.util.Log.e("API_ERROR", "Erro " + response.code() + ": " + errorBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(GerenciarEventosActivity.this, getString(R.string.error_server, response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Evento> call, Throwable t) {
                android.util.Log.e("API_ERROR", "Falha na rede", t);
                Toast.makeText(GerenciarEventosActivity.this, R.string.error_network, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void atualizarEvento(String id, Evento atualizado) {
        apiService.atualizarEvento(id, atualizado).enqueue(new Callback<Evento>() {
            @Override
            public void onResponse(Call<Evento> call, Response<Evento> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GerenciarEventosActivity.this, R.string.msg_event_updated, Toast.LENGTH_SHORT).show();
                    carregarEventos();
                } else {
                    Toast.makeText(GerenciarEventosActivity.this, getString(R.string.error_server, response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Evento> call, Throwable t) {
                Toast.makeText(GerenciarEventosActivity.this, R.string.error_network, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmarExclusao(Evento evento) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.title_delete_event)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage(getString(R.string.msg_confirm_delete, evento.getTitulo()))
            .setPositiveButton(R.string.action_delete, (dialog, which) -> {
                apiService.deletarEvento(evento.getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(GerenciarEventosActivity.this, R.string.msg_event_deleted, Toast.LENGTH_SHORT).show();
                            carregarEventos();
                        } else {
                            Toast.makeText(GerenciarEventosActivity.this, getString(R.string.error_server, response.code()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(GerenciarEventosActivity.this, R.string.error_network, Toast.LENGTH_SHORT).show();
                    }
                });
            })
            .setNegativeButton(R.string.action_cancel, null)
            .show();
    }
}