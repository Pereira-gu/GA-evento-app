package com.unicid.sca_eventos_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

// ESTES SÃO OS IMPORTS QUE RESOLVEM OS ERROS DE SYMBOL
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannerPorteiroActivity extends AppCompatActivity {

    private TextView tvResultado, tvNomeEventoSelecionado;
    private String eventoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_porteiro);

        tvResultado = findViewById(R.id.tvResultado);
        tvNomeEventoSelecionado = findViewById(R.id.tvNomeEventoSelecionado);
        Button btnAbrirScanner = findViewById(R.id.btnAbrirScanner);
        Button btnVoltar = findViewById(R.id.btnVoltarSelecao);

        // Recuperar dados do evento vindos da tela de seleção
        eventoId = getIntent().getStringExtra("EVENTO_ID");
        String eventoNome = getIntent().getStringExtra("EVENTO_NOME");

        if (eventoNome != null) {
            tvNomeEventoSelecionado.setText("Evento: " + eventoNome);
        }

        btnVoltar.setOnClickListener(v -> finish());

        btnAbrirScanner.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
            options.setPrompt("Aponte para o QR Code do Aluno");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            barcodeLauncher.launch(options);
        });
    }

    // Gerenciador do resultado utilizando ScanIntentResult
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                // ScanIntentResult é a classe que contém o conteúdo lido
                ScanIntentResult scanResult = (ScanIntentResult) result;
                if (scanResult.getContents() != null) {
                    validarNoServidor(scanResult.getContents());
                } else {
                    Toast.makeText(this, "Leitura cancelada", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void validarNoServidor(String qrContent) {
        tvResultado.setText("Validando no servidor...");

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        JsonObject requestData = new JsonObject();
        requestData.addProperty("qrCodeData", qrContent); // Formato esperado pelo Backend: UUID;Timestamp
        requestData.addProperty("eventoId", eventoId);

        apiService.validarAcesso(requestData).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvResultado.setText("Status: " + response.body());
                    Toast.makeText(ScannerPorteiroActivity.this, response.body(), Toast.LENGTH_LONG).show();
                } else {
                    tvResultado.setText("Erro: QR Code expirado ou inválido.");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                tvResultado.setText("Falha de conexão: " + t.getMessage());
            }
        });
    }
}