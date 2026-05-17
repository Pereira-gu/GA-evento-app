package com.unicid.sca_eventos_android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import com.unicid.sca_eventos_android.R;

/**
 * Painel principal do Aluno.
 * Exibe opções de acesso ao QR Code, Lista de Eventos e Histórico de presenças.
 */
public class DashboardAlunoActivity extends AppCompatActivity {

    private TextView tvBoasVindas;
    private LinearLayout layoutDashboard;
    private MaterialCardView cardQrCode, cardHistorico, cardEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_aluno);

        tvBoasVindas = findViewById(R.id.tvBoasVindas);
        layoutDashboard = findViewById(R.id.layoutDashboard);
        cardQrCode = findViewById(R.id.cardQrCode);
        cardEventos = findViewById(R.id.cardEventos);
        cardHistorico = findViewById(R.id.cardHistorico);
        Button btnLogout = findViewById(R.id.btnLogout);

        SharedPreferences prefs = getSharedPreferences("SCA_PREFS", MODE_PRIVATE);
        String nome = prefs.getString("USER_NOME", "Aluno");
        boolean isGold = prefs.getBoolean("USER_BADGE_OURO", false);

        if (isGold) {
            tvBoasVindas.setText(getString(R.string.dash_welcome_gold, nome));
            findViewById(R.id.cardConstancia).setVisibility(android.view.View.VISIBLE);
        } else {
            tvBoasVindas.setText(getString(R.string.dash_welcome, nome));
        }

        cardQrCode.setOnClickListener(v -> {
            startActivity(new Intent(this, QrCodeAlunoActivity.class));
        });

        cardEventos.setOnClickListener(v -> {
            startActivity(new Intent(this, ListaEventosAlunoActivity.class));
        });

        cardHistorico.setOnClickListener(v -> {
            startActivity(new Intent(this, HistoricoAlunoActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_logout_title)
                    .setMessage(R.string.dialog_logout_msg)
                    .setPositiveButton(R.string.action_logout, (dialog, which) -> {
                        prefs.edit().clear().apply();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton(R.string.action_cancel, null)
                    .show();
        });
    }
}