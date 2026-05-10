package com.unicid.sca_eventos_android.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.JsonObject;
import com.unicid.sca_eventos_android.R;
import com.unicid.sca_eventos_android.api.ApiClient;
import com.unicid.sca_eventos_android.api.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity responsável pelo cadastro de novos usuários (Alunos ou Porteiros).
 */
public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText etNome = findViewById(R.id.etRegNome);
        EditText etEmail = findViewById(R.id.etRegEmail);
        EditText etSenha = findViewById(R.id.etRegSenha);
        RadioButton rbAluno = findViewById(R.id.rbAluno);
        Button btnRegistrar = findViewById(R.id.btnFinalizarRegistro);
        Button btnVoltar = findViewById(R.id.btnVoltarLogin);

        btnVoltar.setOnClickListener(v -> finish());

        btnRegistrar.setOnClickListener(v -> {
            String nome = etNome.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String senha = etSenha.getText().toString().trim();
            String perfil = rbAluno.isChecked() ? "CLIENTE" : "PORTEIRO";

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, R.string.error_fill_fields, Toast.LENGTH_SHORT).show();
            } else {
                btnRegistrar.setEnabled(false);
                btnRegistrar.setText(R.string.msg_processing);
                enviarRegistro(nome, email, senha, perfil, btnRegistrar);
            }
        });
    }

    private void enviarRegistro(String nome, String email, String senha, String perfil, Button btnRegistrar) {
        JsonObject json = new JsonObject();
        json.addProperty("nome", nome);
        json.addProperty("email", email);
        json.addProperty("senha", senha);
        json.addProperty("perfil", perfil);

        ApiClient.getClient().create(ApiService.class).registrar(json).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                btnRegistrar.setEnabled(true);
                btnRegistrar.setText(R.string.action_register);
                
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, R.string.msg_register_success, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e("SCA_ERRO", "Código de erro: " + response.code());
                    Toast.makeText(RegisterActivity.this, getString(R.string.error_server, response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                btnRegistrar.setEnabled(true);
                btnRegistrar.setText(R.string.action_register);
                Toast.makeText(RegisterActivity.this, getString(R.string.error_network) + ": " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}