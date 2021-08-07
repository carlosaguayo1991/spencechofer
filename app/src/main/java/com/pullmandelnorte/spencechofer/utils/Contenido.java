package com.pullmandelnorte.spencechofer.utils;

import android.content.Context;

import com.orm.SugarContext;
import com.orm.SugarRecord;
import com.pullmandelnorte.spencechofer.entities.Registro;
import com.pullmandelnorte.spencechofer.entities.Viaje;
import com.pullmandelnorte.spencechofer.modelo.Cedula;
import com.pullmandelnorte.spencechofer.modelo.ModeloIntermedio;
import com.pullmandelnorte.spencechofer.modelo.ModeloViaje;
import com.pullmandelnorte.spencechofer.ws.api.API;
import com.pullmandelnorte.spencechofer.ws.api.apiServices.ReservaService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class Contenido {
    public static List<Viaje> getViajeList(Context context) {
//        SugarContext.init(context);
        List<Viaje> list = new ArrayList<>();
        Viaje v = new Viaje();
        Cedula cedula = new Cedula();
//        Gson gson = new Gson();
        List<Registro> registro = Registro.listAll(Registro.class);
        if (registro != null && registro.size() > 0) {
            cedula.setCedula(registro.get(0).getCedula());
        }
        ReservaService reservaService = API.getApi().create(ReservaService.class);
        Call<List<Viaje>> callViajes = reservaService.getViajesDisponibles(cedula);
        try {
            Response<List<Viaje>> response = callViajes.execute();
            if (response.isSuccessful()) {
                Viaje.deleteAll(Viaje.class);
                SugarRecord.saveInTx(response.body());
                list = response.body();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<Viaje> getViajeAsignado(Context context) {
        SugarContext.init(context);
        List<Viaje> list = new ArrayList<>();
        Cedula cedula = new Cedula();
        List<Registro> registro = Registro.listAll(Registro.class);
        if (registro != null && registro.size() > 0) {
            cedula.setCedula(registro.get(0).getCedula());
        }
        ReservaService reservaService = API.getApi().create(ReservaService.class);
        Call<Viaje> viajeIniciadoCall = reservaService.getViajeIniciado(cedula);
        try {
            Response<Viaje> response = viajeIniciadoCall.execute();
            if (response.isSuccessful()) {
                list.add(response.body());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<ModeloIntermedio> getIntermedios(Context context, String idviaje) {
        List<ModeloIntermedio> list = new ArrayList<>();
        ModeloViaje modeloViaje = new ModeloViaje(idviaje);
//        ModeloViaje modeloViaje = new ModeloViaje("553");
        ReservaService reservaService = API.getApi().create(ReservaService.class);
        Call<List<ModeloIntermedio>> callIntermedios = reservaService.getViajeIntermedio(modeloViaje);
        try {
            Response<List<ModeloIntermedio>> response = callIntermedios.execute();
            if (response.isSuccessful()) {
                list = response.body();
            } else {
//                Toast.makeText(context,"No hay reservas", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
