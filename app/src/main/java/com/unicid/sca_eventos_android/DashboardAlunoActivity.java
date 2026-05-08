package com.unicid.sca_eventos_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class DashboardAlunoActivity extends AppCompatActivity {

    private TextView tvBoasVindas;
    private LinearLayout layoutDashboard;
    private MaterialCardView cardQrCode, cardHistorico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_aluno);

        tvBoasVindas = findViewById(R.id.tvBoasVindas);
        layoutDashboard = findViewById(R.id.layoutDashboard);
        cardQrCode = findViewById(R.id.cardQrCode);
        cardHistorico = findViewById(R.id.cardHistorico);
        Button btnLogout = findViewById(R.id.btnLogout);

        SharedPreferences prefs = getSharedPreferences("SCA_PREFS", MODE_PRIVATE);
        String nome = prefs.getString("USER_NOME", "Aluno");
        boolean isGold = prefs.getBoolean("USER_BADGE_OURO", false);

        if (isGold) {
            layoutDashboard.setBackgroundColor(Color.parseColor("#FFD700"));
            tvBoasVindas.setText("Olá, " + nome + "! ✨");
            tvBoasVindas.setTextColor(Color.BLACK);
        } else {
            tvBoasVindas.setText("Olá, " + nome + "!");
        }

        cardQrCode.setOnClickListener(v -> {
            startActivity(new Intent(this, QrCodeAlunoActivity.class));
        });

        cardHistorico.setOnClickListener(v -> {
            startActivity(new Intent(this, HistoricoAlunoActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}