package com.unicid.sca_eventos_android;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                enviarRegistro(nome, email, senha, perfil);
            }
        });
    }

    private void enviarRegistro(String nome, String email, String senha, String perfil) {
        JsonObject json = new JsonObject();
        json.addProperty("nome", nome);
        json.addProperty("email", email);
        json.addProperty("senha", senha);
        json.addProperty("perfil", perfil);

        ApiClient.getClient().create(ApiService.class).registrar(json).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Registro solicitado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Log para descobrir o que o servidor respondeu (ex: 400, 401, 500)
                    Log.e("SCA_ERRO", "Código de erro: " + response.code());
                    Toast.makeText(RegisterActivity.this, "Erro no servidor: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Erro de rede: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}