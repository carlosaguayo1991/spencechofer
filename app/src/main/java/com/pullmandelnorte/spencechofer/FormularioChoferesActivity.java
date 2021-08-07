package com.pullmandelnorte.spencechofer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.pullmandelnorte.spencechofer.entities.Choferesviajes;
import com.pullmandelnorte.spencechofer.entities.Preguntas;
import com.pullmandelnorte.spencechofer.entities.RespuestaChofer;
import com.pullmandelnorte.spencechofer.ws.api.API;
import com.pullmandelnorte.spencechofer.ws.api.apiServices.ReservaService;

import java.util.ArrayList;
import java.util.List;
@RequiresApi(api = Build.VERSION_CODES.N)
public class FormularioChoferesActivity extends AppCompatActivity {
    private ListView lista;
    public static FormularioAdapter formularioAdapter;
    private String idviaje;
    private String id;
    private String chofer;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_choferes);

        //agregamos el toolbar
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        setTitle("");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idviaje = bundle.getString("idviaje");
            id = bundle.getString("id");
            chofer = bundle.getString("chofer");
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
        formularioAdapter = new FormularioAdapter(FormularioChoferesActivity.this, listpre);
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
        //verificar que todas se las preguntas tenga respuesta
        boolean respTotal = true;
        progress = new ProgressDialog(FormularioChoferesActivity.this);
        progress.setTitle("¡Espere por favor!");
        progress.setMessage("Cargando...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        List<Preguntas> preguntasList = Preguntas.listAll(Preguntas.class);
        if (preguntasList != null && preguntasList.size() > 0) {
            RespuestaChofer respuesta = new RespuestaChofer();
            respuesta.setChofer(chofer);
            respuesta.setIdviaje(idviaje);
            respuesta.setIdUsuario(id);
            for (Preguntas aux : preguntasList) {
                switch (aux.getColumna()) {
                    case "campo1":
                        respuesta.setCampo1(aux.getCampo());
                        respuesta.setTexto1(aux.getTexto());
                        respuesta.setRespuesta1(aux.getRespuesta());
                        if(aux.getCampo().equals("true") && aux.getRespuesta() == null){
                            respTotal = false;
                        }
                        break;
                    case "campo2":
                        respuesta.setCampo2(aux.getCampo());
                        respuesta.setTexto2(aux.getTexto());
                        respuesta.setRespuesta2(aux.getRespuesta());
                        if(aux.getCampo().equals("true") && aux.getRespuesta() == null){
                            respTotal = false;
                        }
                        break;
                    case "campo3":
                        respuesta.setCampo3(aux.getCampo());
                        respuesta.setTexto3(aux.getTexto());
                        respuesta.setRespuesta3(aux.getRespuesta());
                        if(aux.getCampo().equals("true") && aux.getRespuesta() == null){
                            respTotal = false;
                        }
                        break;
                    case "campo4":
                        respuesta.setCampo4(aux.getCampo());
                        respuesta.setTexto4(aux.getTexto());
                        respuesta.setRespuesta4(aux.getRespuesta());
                        if(aux.getCampo().equals("true") && aux.getRespuesta() == null){
                            respTotal = false;
                        }
                        break;
                    case "campo5":
                        respuesta.setCampo5(aux.getCampo());
                        respuesta.setTexto5(aux.getTexto());
                        respuesta.setRespuesta5(aux.getRespuesta());
                        if(aux.getCampo().equals("true") && aux.getRespuesta() == null){
                            respTotal = false;
                        }
                        break;
                    case "campo6":
                        respuesta.setCampo6(aux.getCampo());
                        respuesta.setTexto6(aux.getTexto());
                        respuesta.setRespuesta6(aux.getRespuesta());
                        if(aux.getCampo().equals("true") && aux.getRespuesta() == null){
                            respTotal = false;
                        }
                        break;
                    case "campo7":
                        respuesta.setCampo7(aux.getCampo());
                        respuesta.setTexto7(aux.getTexto());
                        respuesta.setRespuesta7(aux.getRespuesta());
                        if(aux.getCampo().equals("true") && aux.getRespuesta() == null){
                            respTotal = false;
                        }
                        break;
                    case "campo8":
                        respuesta.setCampo8(aux.getCampo());
                        respuesta.setTexto8(aux.getTexto());
                        respuesta.setRespuesta8(aux.getRespuesta());
                        if(aux.getCampo().equals("true") && aux.getRespuesta() == null){
                            respTotal = false;
                        }
                        break;
                    case "campo9":
                        respuesta.setCampo9(aux.getCampo());
                        respuesta.setTexto9(aux.getTexto());
                        respuesta.setRespuesta9(aux.getRespuesta());
                        if(aux.getCampo().equals("true") && aux.getRespuesta() == null){
                            respTotal = false;
                        }
                        break;
                    case "campo10":
                        respuesta.setCampo10(aux.getCampo());
                        respuesta.setTexto10(aux.getTexto());
                        respuesta.setRespuesta10(aux.getRespuesta());
                        if(aux.getCampo().equals("true") && aux.getRespuesta() == null){
                            respTotal = false;
                        }
                        break;
                }
            }

            if(respTotal){
                enviarEncuesta(FormularioChoferesActivity.this,respuesta);
            }else{
                progress.dismiss();
                Toast.makeText(FormularioChoferesActivity.this,"Responder todas las preguntas",Toast.LENGTH_SHORT).show();
            }


//            ReservaService rs = API.getApi().create(ReservaService.class);
//            Call<ResponseBody> cuestionarioCall = rs.funCargaCuestionario(respuesta);
//            cuestionarioCall.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    if (response.isSuccessful()) {
//                        Toast.makeText(FormularioChoferesActivity.this, "Formulario enviado con éxito", Toast.LENGTH_SHORT).show();
//                        progress.dismiss();
//                        finish();
//                    } else {
//                        progress.dismiss();
//                        Toast.makeText(FormularioChoferesActivity.this, "Error:" + response.code(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    Toast.makeText(FormularioChoferesActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    progress.dismiss();
//                }
//            });
        } else {
            Toast.makeText(FormularioChoferesActivity.this, "No hay Respuestas", Toast.LENGTH_SHORT).show();
        }


    }

    public void enviarEncuesta(Context context, RespuestaChofer respuesta) {
        ReservaService rs = API.getApi().create(ReservaService.class);
        Call<ResponseBody> cuestionarioCall = null;
        if(respuesta.getChofer().equals("PRINCIPAL")){
            cuestionarioCall = rs.funCargaCuestionarioCp(respuesta);
        }else if(respuesta.getChofer().equals("SECUNDARIO")){
            cuestionarioCall = rs.funCargaCuestionarioSc(respuesta);
        }else{
            cuestionarioCall = rs.funCargaCuestionarioAux(respuesta);
        }
        cuestionarioCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(FormularioChoferesActivity.this, "Formulario enviado con éxito", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                    List<Choferesviajes> cv = Choferesviajes.listAll(Choferesviajes.class);
                    if(respuesta.getChofer().equals("PRINCIPAL")){
                        Choferesviajes chov = Choferesviajes.findById(Choferesviajes.class,cv.get(0).getId());
                        chov.setRealizado_principal(true);
                        chov.save();
                        EncuestaChoferes.adapter.notifyDataSetChanged();
                    }else if(respuesta.getChofer().equals("SECUNDARIO")){
                        Choferesviajes chov = Choferesviajes.findById(Choferesviajes.class,cv.get(0).getId());
                        chov.setRealizado_secundario(true);
                        chov.save();
                    }else{
                        Choferesviajes chov = Choferesviajes.findById(Choferesviajes.class,cv.get(0).getId());
                        chov.setRealizado_auxiliar(true);
                        chov.save();
                    }
                    //actualizar adapter
                    List<String> listEncuestaChofer = new ArrayList<>();
                    List<Choferesviajes> listChoferes = Choferesviajes.listAll(Choferesviajes.class);
                    if(listChoferes != null && listChoferes.size() > 0){
                        if(listChoferes.get(0).getCondicion_chofer_principal().equals("true")){
//                            listEncuesta.add(listChoferes.get(0).getNombre_principal());
//                            listEncuestaid.add(listChoferes.get(0).getChoferes_principal());
                            listEncuestaChofer.add("PRINCIPAL");
                        };
                        if(listChoferes.get(0).getCondicion_chofer_secundaria().equals("true")){
//                            listEncuesta.add(listChoferes.get(0).getNombre_chofer_secundaria());
//                            listEncuestaid.add(listChoferes.get(0).getChoferes_secundaria());
                            listEncuestaChofer.add("SECUNDARIO");
                        };
                        if(listChoferes.get(0).getCondicion_auxiliar().equals("true")){
//                            listEncuesta.add(listChoferes.get(0).getNombre_auxiliar());
//                            listEncuestaid.add(listChoferes.get(0).getAuxiliar());
                            listEncuestaChofer.add("AUXILIAR");
                        };
                        EncuestaChoferes.adapter.updateData(listEncuestaChofer);
//                        EncuestaChoferes.adapter.notifyDataSetChanged();
                    }


                    finish();
                } else {
                    progress.dismiss();
                    Toast.makeText(FormularioChoferesActivity.this, "Error:" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(FormularioChoferesActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });
    }
}