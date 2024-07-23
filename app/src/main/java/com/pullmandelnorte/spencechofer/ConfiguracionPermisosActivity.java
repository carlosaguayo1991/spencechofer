package com.pullmandelnorte.spencechofer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ConfiguracionPermisosActivity extends AppCompatActivity {
    private TextView siguiente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_permisos);
        siguiente = findViewById(R.id.activity_wizard_media_next);


    }

    public void siguiente(View view) {
        finish();
    }
}