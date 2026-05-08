package com.unicid.sca_eventos_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.unicid.sca_eventos_android.R;

/**
 * Activity inicial do aplicativo (Splash Screen).
 * Sua função é exibir a marca do sistema por alguns instantes e redirecionar
 * o usuário para a tela de Login.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Define o layout da Splash Screen (poderia ser um novo layout com logo)
        // Usando o layout de login temporariamente como placeholder
        setContentView(R.layout.activity_login);

        // Remove o texto e botões para parecer uma splash se necessário, 
        // ou apenas redireciona após 2 segundos.
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}