package com.pullmandelnorte.spencechofer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EscaneoSeleccionActivity extends AppCompatActivity {
    private Button boton1;
//    private Button boton2;
    private String idviaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaneo_seleccion);
        boton1 = findViewById(R.id.button);
//        boton2 =  findViewById(R.id.button2);

        Bundle bundle =  getIntent().getExtras();
        if(bundle != null){
            idviaje =  bundle.getString("idviaje");
        }


        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EscaneoActivity.class);
                intent.putExtra("idviaje", idviaje);
                startActivity(intent);
            }
        });
//
//        boton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(),EscaneoInternoActivity.class);
//                intent.putExtra("idviaje", idviaje);
//                startActivity(intent);
//            }
//        });
    }
}