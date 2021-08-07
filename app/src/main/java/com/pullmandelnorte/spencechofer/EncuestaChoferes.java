package com.pullmandelnorte.spencechofer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.orm.SugarRecord;
import com.pullmandelnorte.spencechofer.adapter.EncuestaChoferAdapter;
import com.pullmandelnorte.spencechofer.entities.Choferesviajes;
import com.pullmandelnorte.spencechofer.entities.Preguntas;
import com.pullmandelnorte.spencechofer.ws.api.API;
import com.pullmandelnorte.spencechofer.ws.api.apiServices.ReservaService;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EncuestaChoferes extends AppCompatActivity {
    private ListView lista;
    private List<Choferesviajes> listChoferes;
    private List<String> listEncuesta = new ArrayList<>();
    private List<String> listEncuestaid = new ArrayList<>();
    private List<String> listEncuestaChofer = new ArrayList<>();
    private ProgressDialog progress;
    private String idviaje;
    private boolean encueRealizada;
//    public static ArrayAdapter<String> adapter;
    public static EncuestaChoferAdapter adapter;
    public BuscarViaje buscarViaje = new BuscarViaje();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta_choferes);
        lista = findViewById(R.id.list);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idviaje = bundle.getString("idviaje");
        }
        listChoferes = Choferesviajes.listAll(Choferesviajes.class);
        if(listChoferes != null && listChoferes.size() > 0){
            if(listChoferes.get(0).getCondicion_chofer_principal().equals("true")){
                listEncuesta.add(listChoferes.get(0).getNombre_principal());
                listEncuestaid.add(listChoferes.get(0).getChoferes_principal());
                listEncuestaChofer.add("PRINCIPAL");
            };
            if(listChoferes.get(0).getCondicion_chofer_secundaria().equals("true")){
                listEncuesta.add(listChoferes.get(0).getNombre_chofer_secundaria());
                listEncuestaid.add(listChoferes.get(0).getChoferes_secundaria());
                listEncuestaChofer.add("SECUNDARIO");
            };
            if(listChoferes.get(0).getCondicion_auxiliar().equals("true")){
                listEncuesta.add(listChoferes.get(0).getNombre_auxiliar());
                listEncuestaid.add(listChoferes.get(0).getAuxiliar());
                listEncuestaChofer.add("AUXILIAR");
            };
        }
//        adapter  = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listEncuesta);
        adapter  = new EncuestaChoferAdapter(this,listEncuesta,listEncuestaChofer);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //buscar que chofer selecciono
//                String chofer = null;
//                if(parent.getItemIdAtPosition(position) == 0){
//                    chofer = "PRINCIPAL";
//                }else if(parent.getItemIdAtPosition(position) == 1){
//                    chofer = "SECUNDARIO";
//                }else{
//                    chofer = "AUXILIAR";
//                }
                //verificar si ya realizo encuesta
                List<Choferesviajes> chofEncue = Choferesviajes.listAll(Choferesviajes.class);
                if(listEncuestaChofer.get(position).equals("PRINCIPAL")){
                    if(chofEncue.get(0).isRealizado_principal()){
                        encueRealizada = true;
                    }else{
                        encueRealizada = false;
                    };
                }else if(listEncuestaChofer.get(position).equals("SECUNDARIO")){
                    if(chofEncue.get(0).isRealizado_secundario()){
                        encueRealizada = true;
                    }else{
                        encueRealizada = false;
                    };
                }else{
                    if(chofEncue.get(0).isRealizado_auxiliar()){
                        encueRealizada = true;
                    }else{
                        encueRealizada = false;
                    };
                }

                if(encueRealizada){
                    Toast.makeText(EncuestaChoferes.this,"Encuesta Realizada",Toast.LENGTH_SHORT).show();
                }else{
                    //buscar preguntas
                    progress = new ProgressDialog(EncuestaChoferes.this);
                    progress.setTitle("¡Espere por favor!");
                    progress.setMessage("Cargando...");
                    progress.setCancelable(false);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();
                    ReservaService reservaService1 = API.getApi().create(ReservaService.class);
                    Call<JsonElement> callPreguntas = reservaService1.getPreguntasChofer();
//                    String finalChofer = listEncuestaChofer.get(position);
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
                                Intent intent = new Intent(EncuestaChoferes.this, FormularioChoferesActivity.class);
                                intent.putExtra("idviaje",idviaje);
                                intent.putExtra("id",listEncuestaid.get(position));
                                intent.putExtra("chofer", listEncuestaChofer.get(position));
                                startActivity(intent);
                            }else{
                                progress.dismiss();
                                Preguntas.deleteAll(Preguntas.class);
                                Toast.makeText(EncuestaChoferes.this,"Error :" + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            progress.dismiss();
                            Toast.makeText(EncuestaChoferes.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


    }

    public void finalizar(View v){
        //verificar si las encuesta ya fueron realizadas para ese viaje
        boolean encuestaRealizada;
        List<Choferesviajes> encuesta = Choferesviajes.find(Choferesviajes.class, "IDVIAJE = ? ",idviaje);
        if (encuesta != null && encuesta.size() > 0) {
            encuestaRealizada = true;
            if(encuesta.get(0).getCondicion_chofer_principal().equals("true")){
                if (encuesta.get(0).isRealizado_principal()) {
                    encuestaRealizada = true;
                }else {
                    encuestaRealizada = false;
                }
            };
            if(encuestaRealizada){
                if(encuesta.get(0).getCondicion_chofer_secundaria().equals("true")){
                    if (encuesta.get(0).isRealizado_secundario()) {
                        encuestaRealizada = true;
                    }else {
                        encuestaRealizada = false;
                    }
                };
            }
            if(encuestaRealizada){
                if(encuesta.get(0).getCondicion_auxiliar().equals("true")){
                    if (encuesta.get(0).isRealizado_auxiliar()) {
                        encuestaRealizada = true;
                    }else {
                        encuestaRealizada = false;
                    }
                };
            }

        } else {
            encuestaRealizada = false;
        }

        if (encuestaRealizada) {
            buscarViaje.viajeAdapter.iniciarViaje(EncuestaChoferes.this,idviaje);
            Choferesviajes.deleteAll(Choferesviajes.class);
            finish();


        } else {
            Toast.makeText(v.getContext(), "Responder Todas las encuestas", Toast.LENGTH_SHORT).show();
        }
    }
}