package com.unicid.sca_eventos_android.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.JsonObject;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;
import com.unicid.sca_eventos_android.R;
import com.unicid.sca_eventos_android.api.ApiClient;
import com.unicid.sca_eventos_android.api.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity que realiza a leitura e validação de QR Codes dos alunos.
 * Utiliza a câmera para escanear e o backend para validar o acesso ao evento selecionado.
 */
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

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
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
        requestData.addProperty("qrCodeData", qrContent);
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