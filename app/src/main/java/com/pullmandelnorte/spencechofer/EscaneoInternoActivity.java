package com.pullmandelnorte.spencechofer;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pullmandelnorte.spencechofer.modelo.Mensaje;
import com.pullmandelnorte.spencechofer.modelo.ModeloLectura;
import com.pullmandelnorte.spencechofer.ws.api.API;
import com.pullmandelnorte.spencechofer.ws.api.apiServices.ReservaService;

import java.io.IOException;

public class EscaneoInternoActivity extends AppCompatActivity {
    public TextView resultado;
    private EditText cod_barra;
    private TextView atras;
    private String idviaje;
    private ProgressBar progressBar;
    private AlertDialog dialog;
    private final Handler handler = new Handler();
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaneo_interno);
        resultado = findViewById(R.id.resultado);
        cod_barra =  findViewById(R.id.cod_barra);
        atras = findViewById(R.id.atras);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        cod_barra.requestFocus();
        cod_barra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Toast.makeText(EscaneoInternoActivity.this,"beforeTextChanged : " + charSequence ,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals("")){
                    if(runnable != null){
                        handler.removeCallbacks(runnable);
                    }
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            setProgressDialog();

                            ModeloLectura modeloLectura = new ModeloLectura();
                            modeloLectura.setIdviajes(idviaje);
                            modeloLectura.setCod_barra(charSequence.toString());
                            ReservaService reservaService = API.getApi().create(ReservaService.class);
                            Call<Mensaje> reservaServiceCall = reservaService.funLectura(modeloLectura);
                            reservaServiceCall.enqueue(new Callback<Mensaje>() {
                                @Override
                                public void onResponse(Call<Mensaje> call, Response<Mensaje> response) {
                                    if(response.isSuccessful()){
                                        resultado.setText(response.body().getMensaje());
                                    }
                                    dialog.dismiss();
                                    cod_barra.setText("");

                                }

                                @Override
                                public void onFailure(Call<Mensaje> call, Throwable t) {
                                    dialog.dismiss();
                                    Toast.makeText(EscaneoInternoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    };
                    handler.postDelayed(runnable,500);
//                Toast.makeText(EscaneoInternoActivity.this,"onTextChanged : " + charSequence ,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

//                Toast.makeText(EscaneoInternoActivity.this,"afterTextChanged : "  ,Toast.LENGTH_LONG).show();
            }
        });

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    public void setProgressDialog() {
        int llPadding = 30;
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(this);
        tvText.setText("Cargando ...");
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(ll);

        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }
    }
}