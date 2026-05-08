package com.unicid.sca_eventos_android;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrCodeAlunoActivity extends AppCompatActivity {

    private ImageView ivQrCode;
    private TextView tvTimer;
    private CountDownTimer countDownTimer;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_aluno);

        ivQrCode = findViewById(R.id.ivQrCode);
        tvTimer = findViewById(R.id.tvTimer);
        Button btnVoltar = findViewById(R.id.btnVoltarDash);

        SharedPreferences prefs = getSharedPreferences("SCA_PREFS", MODE_PRIVATE);
        userId = prefs.getString("USER_ID", "");

        iniciarCicloQrCode();

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void iniciarCicloQrCode() {
        if (countDownTimer != null) countDownTimer.cancel();
        atualizarQrCode();

        countDownTimer = new CountDownTimer(300000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                tvTimer.setText(String.format("O código expira em: %02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                iniciarCicloQrCode();
            }
        }.start();
    }

    private void atualizarQrCode() {
        long timestamp = System.currentTimeMillis() / 1000L;
        String qrData = userId + ";" + timestamp;

        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(qrData, BarcodeFormat.QR_CODE, 512, 512);
            Bitmap bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565);

            for (int x = 0; x < 512; x++) {
                for (int y = 0; y < 512; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            ivQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}