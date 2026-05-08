package com.unicid.sca_eventos_android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.JsonObject;
import com.unicid.sca_eventos_android.R;
import com.unicid.sca_eventos_android.api.ApiClient;
import com.unicid.sca_eventos_android.api.ApiService;
import com.unicid.sca_eventos_android.models.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity responsável pelo controle de acesso ao aplicativo.
 * Gerencia a autenticação do usuário e a persistência da sessão.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etSenha;
    private Button btnLogin;
    private TextView tvRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("SCA_PREFS", MODE_PRIVATE);
        if (prefs.contains("USER_ID")) {
            direcionarUsuario(prefs.getString("USER_PERFIL", "CLIENTE"));
            return;
        }

        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegistro = findViewById(R.id.tvRegistro);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String senha = etSenha.getText().toString().trim();
            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                executarLogin(email, senha);
            }
        });

        tvRegistro.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void executarLogin(String email, String senha) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        JsonObject loginData = new JsonObject();
        loginData.addProperty("email", email);
        loginData.addProperty("senha", senha);

        apiService.login(loginData).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse user = response.body();

                    SharedPreferences.Editor editor = getSharedPreferences("SCA_PREFS", MODE_PRIVATE).edit();
                    editor.putString("USER_ID", user.getId());
                    editor.putString("USER_NOME", user.getNome());
                    editor.putString("USER_PERFIL", user.getPerfil());
                    editor.putBoolean("USER_BADGE_OURO", user.isBadgeOuro());
                    editor.apply();

                    Toast.makeText(LoginActivity.this, "Bem-vindo, " + user.getNome(), Toast.LENGTH_SHORT).show();
                    direcionarUsuario(user.getPerfil());
                } else {
                    Toast.makeText(LoginActivity.this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Erro de rede", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void direcionarUsuario(String perfil) {
        Intent intent;
        if ("PORTEIRO".equals(perfil)) {
            intent = new Intent(this, SelecaoEventoActivity.class);
        } else {
            intent = new Intent(this, DashboardAlunoActivity.class);
        }
        startActivity(intent);
        finish();
    }
}