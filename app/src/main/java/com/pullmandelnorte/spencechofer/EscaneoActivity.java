package com.pullmandelnorte.spencechofer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.orm.SugarRecord;
import com.pullmandelnorte.spencechofer.entities.Preguntas;
import com.pullmandelnorte.spencechofer.modelo.Mensaje;
import com.pullmandelnorte.spencechofer.modelo.ModeloQr;
import com.pullmandelnorte.spencechofer.ws.api.API;
import com.pullmandelnorte.spencechofer.ws.api.apiServices.ReservaService;

import java.util.ArrayList;
import java.util.List;

public class EscaneoActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnScanner;
//    private TextView resultado;
    public static TextView resultado;
    private String idviaje;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaneo);
        btnScanner = findViewById(R.id.button);
        resultado = findViewById(R.id.resultado);
        btnScanner.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idviaje = bundle.getString("idviaje");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null)
            if (intentResult.getContents() != null) {
//                resultado.setText(intentResult.getContents());
                Gson gson = new Gson();
                ModeloQr modeloQr = new ModeloQr();
                modeloQr.setIdviaje(idviaje);
                modeloQr.setQr(intentResult.getContents());
                String valor = gson.toJson(modeloQr);
                ReservaService reservaService = API.getApi().create(ReservaService.class);
                Call<Mensaje> reservaServiceCall = reservaService.enviarCodigoQr(modeloQr);
                reservaServiceCall.enqueue(new Callback<Mensaje>() {
                    @Override
                    public void onResponse(Call<Mensaje> call, Response<Mensaje> response) {
                        if (response.isSuccessful()) {
                            resultado.setText(response.body().getMensaje());
//                            resultado.setText("Lectura Exitosa");
                            Preguntas pre = null;
                            //buscar preguntas
                            progress = new ProgressDialog(EscaneoActivity.this);
                            progress.setTitle("¡Espere por favor!");
                            progress.setMessage("Cargando...");
                            progress.setCancelable(false);
                            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progress.show();
                            ReservaService reservaService1 = API.getApi().create(ReservaService.class);
                            Call<JsonElement> callPreguntas = reservaService1.getPreguntas();
                            callPreguntas.enqueue(new Callback<JsonElement>() {
                                @Override
                                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                    if (response.isSuccessful()) {
                                        Preguntas pre = null;
                                        progress.dismiss();
                                        List<Preguntas> listPreguntas = new ArrayList<>();
                                        Preguntas.deleteAll(Preguntas.class);
                                        JsonObject jsonObject = response.body().getAsJsonObject();
                                        pre = new Preguntas();
                                        pre.setCampo(jsonObject.get("campo1").getAsString());
                                        pre.setTexto(jsonObject.get("texto1").getAsString());
                                        pre.setColumna("campo1");
                                        pre.setTipodato("STRING");
                                        listPreguntas.add(pre);
                                        pre = new Preguntas();
                                        pre.setCampo(jsonObject.get("campo2").getAsString());
                                        pre.setTexto(jsonObject.get("texto2").getAsString());
                                        pre.setColumna("campo2");
                                        pre.setTipodato("STRING");
                                        listPreguntas.add(pre);
                                        pre = new Preguntas();
                                        pre.setCampo(jsonObject.get("campo3").getAsString());
                                        pre.setTexto(jsonObject.get("texto3").getAsString());
                                        pre.setColumna("campo3");
                                        pre.setTipodato("STRING");
                                        listPreguntas.add(pre);
                                        pre = new Preguntas();
                                        pre.setCampo(jsonObject.get("campo4").getAsString());
                                        pre.setTexto(jsonObject.get("texto4").getAsString());
                                        pre.setColumna("campo4");
                                        pre.setTipodato("STRING");
                                        listPreguntas.add(pre);
                                        pre = new Preguntas();
                                        pre.setCampo(jsonObject.get("campo5").getAsString());
                                        pre.setTexto(jsonObject.get("texto5").getAsString());
                                        pre.setColumna("campo5");
                                        pre.setTipodato("STRING");
                                        listPreguntas.add(pre);
                                        pre = new Preguntas();
                                        pre.setCampo(jsonObject.get("campo6").getAsString());
                                        pre.setTexto(jsonObject.get("texto6").getAsString());
                                        pre.setColumna("campo6");
                                        pre.setTipodato("BOOLEAN");
                                        listPreguntas.add(pre);
                                        pre = new Preguntas();
                                        pre.setCampo(jsonObject.get("campo7").getAsString());
                                        pre.setTexto(jsonObject.get("texto7").getAsString());
                                        pre.setColumna("campo7");
                                        pre.setTipodato("BOOLEAN");
                                        listPreguntas.add(pre);
                                        pre = new Preguntas();
                                        pre.setCampo(jsonObject.get("campo8").getAsString());
                                        pre.setTexto(jsonObject.get("texto8").getAsString());
                                        pre.setColumna("campo8");
                                        pre.setTipodato("BOOLEAN");
                                        listPreguntas.add(pre);
                                        pre = new Preguntas();
                                        pre.setCampo(jsonObject.get("campo9").getAsString());
                                        pre.setTexto(jsonObject.get("texto9").getAsString());
                                        pre.setColumna("campo9");
                                        pre.setTipodato("BOOLEAN");
                                        listPreguntas.add(pre);
                                        pre = new Preguntas();
                                        pre.setCampo(jsonObject.get("campo10").getAsString());
                                        pre.setTexto(jsonObject.get("texto10").getAsString());
                                        pre.setColumna("campo10");
                                        pre.setTipodato("BOOLEAN");
                                        listPreguntas.add(pre);
                                        SugarRecord.saveInTx(listPreguntas);
                                        Intent intent = new Intent(EscaneoActivity.this, FormularioPreguntasActivity.class);
                                        intent.putExtra("idviaje",idviaje);
                                        intent.putExtra("qr",intentResult.getContents());
                                        intent.putExtra("titulo", resultado.getText().toString());
                                        startActivity(intent);
                                    }else{
                                        progress.dismiss();
                                        Preguntas.deleteAll(Preguntas.class);
                                        Toast.makeText(EscaneoActivity.this,"Error :" + response.code(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonElement> call, Throwable t) {
                                    progress.dismiss();
                                    Toast.makeText(EscaneoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            resultado.setText("Lectura Inválida");
                        }
                    }

                    @Override
                    public void onFailure(Call<Mensaje> call, Throwable t) {
                        Toast.makeText(EscaneoActivity.this, "Error en la consulta", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                resultado.setText("ERROR");
            }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                resultado.setText("");
                new IntentIntegrator(EscaneoActivity.this).initiateScan();
                break;
        }
    }
}
