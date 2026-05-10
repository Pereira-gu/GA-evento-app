package com.unicid.sca_eventos_android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import com.unicid.sca_eventos_android.R;

/**
 * Painel principal do Organizador.
 * Permite acessar o gerenciamento de eventos e visualização de inscritos.
 */
public class DashboardOrganizadorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_organizador);

        MaterialCardView cardGerenciar = findViewById(R.id.cardGerenciarEventos);
        MaterialCardView cardInscricoes = findViewById(R.id.cardVerInscricoes);
        Button btnLogout = findViewById(R.id.btnLogoutOrg);

        cardGerenciar.setOnClickListener(v -> {
            startActivity(new Intent(this, GerenciarEventosActivity.class));
        });

        cardInscricoes.setOnClickListener(v -> {
            startActivity(new Intent(this, SelecionarEventoInscritosActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Sair")
                    .setMessage("Deseja realmente sair da conta?")
                    .setPositiveButton("Sair", (dialog, which) -> {
                        SharedPreferences prefs = getSharedPreferences("SCA_PREFS", MODE_PRIVATE);
                        prefs.edit().clear().apply();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }
}