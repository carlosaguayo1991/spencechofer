package com.pullmandelnorte.spencechofer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pullmandelnorte.spencechofer.adapter.FormularioAdapter;
import com.pullmandelnorte.spencechofer.entities.Preguntas;
import com.pullmandelnorte.spencechofer.entities.Respuesta;
import com.pullmandelnorte.spencechofer.ws.api.API;
import com.pullmandelnorte.spencechofer.ws.api.apiServices.ReservaService;

import java.util.List;

public class FormularioPreguntasActivity extends AppCompatActivity {
    private ListView lista;
    public static FormularioAdapter formularioAdapter;
    private String idviaje;
    private String qr;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_preguntas);
        //agregamos el toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idviaje = bundle.getString("idviaje");
            qr = bundle.getString("qr");
        }

        //Crea contenedor
        LinearLayout contenedor = (LinearLayout) findViewById(R.id.layout_titulo);
        contenedor.setLayoutParams(new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        contenedor.setOrientation(LinearLayout.VERTICAL);
        contenedor.setGravity(Gravity.CENTER);
//        //Crea ImageView y TextView
        TextView miTextView = new TextView(getApplicationContext());
//        //Agrega propiedades al TextView.
        miTextView.setText("Formulario");
        miTextView.setTextSize(25);
        miTextView.setGravity(Gravity.CENTER);

//        //Agrega vistas al contenedor.
        contenedor.addView(miTextView);
        lista = (ListView) findViewById(R.id.lista);
        //trae todos los datos de la tabla preguntas
        List<Preguntas> listpre = Preguntas.find(Preguntas.class, " CAMPO = ?", "true");
        formularioAdapter = new FormularioAdapter(FormularioPreguntasActivity.this, listpre);
//                        SwipeDismissAdapter swipeDismissAdapter = new SwipeDismissAdapter(visitasAdapter, new OnDismissCallback() {
//                            @Override
//                            public void onDismiss(@NonNull ViewGroup listView, @NonNull int[] reverseSortedPositions) {
//
//                            }
//                        });

//        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
//                auditoriaPreguntasAdapter);
//        swingBottomInAnimationAdapter.setAbsListView(lista);
        lista.setAdapter(formularioAdapter);
//        assert swingBottomInAnimationAdapter.getViewAnimator() != null;

//        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(
//                INITIAL_DELAY_MILLIS);

        lista.setClipToPadding(false);
        lista.setDivider(null);
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                8, r.getDisplayMetrics());
        lista.setDividerHeight(px);
        lista.setFadingEdgeLength(0);
        lista.setFitsSystemWindows(true);
        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                r.getDisplayMetrics());
        lista.setPadding(px, px, px, px);
        lista.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
    }

    public void guardar(View v) {
        ProgressDialog progress = new ProgressDialog(FormularioPreguntasActivity.this);
        progress.setTitle("¡Espere por favor!");
        progress.setMessage("Cargando...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        List<Preguntas> preguntasList = Preguntas.listAll(Preguntas.class);
        if (preguntasList != null && preguntasList.size() > 0) {
            Respuesta respuesta = new Respuesta();
            respuesta.setIdviaje(idviaje);
            respuesta.setQr(qr);
            for (Preguntas aux : preguntasList) {
                switch (aux.getColumna()) {
                    case "campo1":
                        respuesta.setCampo1(aux.getCampo());
                        respuesta.setTexto1(aux.getTexto());
                        respuesta.setRespuesta1(aux.getRespuesta());
                        break;
                    case "campo2":
                        respuesta.setCampo2(aux.getCampo());
                        respuesta.setTexto2(aux.getTexto());
                        respuesta.setRespuesta2(aux.getRespuesta());
                        break;
                    case "campo3":
                        respuesta.setCampo3(aux.getCampo());
                        respuesta.setTexto3(aux.getTexto());
                        respuesta.setRespuesta3(aux.getRespuesta());
                        break;
                    case "campo4":
                        respuesta.setCampo4(aux.getCampo());
                        respuesta.setTexto4(aux.getTexto());
                        respuesta.setRespuesta4(aux.getRespuesta());
                        break;
                    case "campo5":
                        respuesta.setCampo5(aux.getCampo());
                        respuesta.setTexto5(aux.getTexto());
                        respuesta.setRespuesta5(aux.getRespuesta());
                        break;
                    case "campo6":
                        respuesta.setCampo6(aux.getCampo());
                        respuesta.setTexto6(aux.getTexto());
                        respuesta.setRespuesta6(aux.getRespuesta());
                        break;
                    case "campo7":
                        respuesta.setCampo7(aux.getCampo());
                        respuesta.setTexto7(aux.getTexto());
                        respuesta.setRespuesta7(aux.getRespuesta());
                        break;
                    case "campo8":
                        respuesta.setCampo8(aux.getCampo());
                        respuesta.setTexto8(aux.getTexto());
                        respuesta.setRespuesta8(aux.getRespuesta());
                        break;
                    case "campo9":
                        respuesta.setCampo9(aux.getCampo());
                        respuesta.setTexto9(aux.getTexto());
                        respuesta.setRespuesta9(aux.getRespuesta());
                        break;
                    case "campo10":
                        respuesta.setCampo10(aux.getCampo());
                        respuesta.setTexto10(aux.getTexto());
                        respuesta.setRespuesta10(aux.getRespuesta());
                        break;
                }
            }

            ReservaService rs = API.getApi().create(ReservaService.class);
            Call<ResponseBody> cuestionarioCall = rs.funCargaCuestionario(respuesta);
            cuestionarioCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(FormularioPreguntasActivity.this, "Formulario enviado con éxito", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                        finish();
                    } else {
                        progress.dismiss();
                        Toast.makeText(FormularioPreguntasActivity.this, "Error:" + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(FormularioPreguntasActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            });
        } else {
            Toast.makeText(FormularioPreguntasActivity.this, "No hay Respuestas", Toast.LENGTH_SHORT).show();
        }


    }
}