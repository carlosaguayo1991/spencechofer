package com.pullmandelnorte.spencechofer.ws.api.apiServices;

import com.google.gson.JsonElement;
import com.pullmandelnorte.spencechofer.entities.Choferesviajes;
import com.pullmandelnorte.spencechofer.entities.Respuesta;
import com.pullmandelnorte.spencechofer.entities.RespuestaChofer;
import com.pullmandelnorte.spencechofer.entities.Viaje;
import com.pullmandelnorte.spencechofer.modelo.Cedula;
import com.pullmandelnorte.spencechofer.modelo.Mensaje;
import com.pullmandelnorte.spencechofer.modelo.ModeloIntermedio;
import com.pullmandelnorte.spencechofer.modelo.ModeloQr;
import com.pullmandelnorte.spencechofer.modelo.ModeloViaje;
import com.pullmandelnorte.spencechofer.modelo.ModeloViajeId;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ReservaService {
    @POST("conductores/funListaViajesDIsponibles.php")
    Call<List<Viaje>> getViajesDisponibles(@Body Cedula cedula);

    @POST("conductores/funIniciarViaje.php")
    Call<Mensaje> iniciarviaje(@Body ModeloViaje modeloViaje);

    @POST("conductores/funConsultaAsignacion.php")
    Call<Viaje> getViajeIniciado(@Body Cedula cedula);

    @POST("reservas/funIntermedios.php")
    Call<List<ModeloIntermedio>> getViajeIntermedio(@Body ModeloViaje modeloViaje);

    @POST("conductores/funFinalizarViaje.php")
    Call<Mensaje> finalizarviaje(@Body ModeloViaje modeloViaje);

    @POST("conductores/funLecturaQr.php")
    Call<Mensaje> enviarCodigoQr(@Body ModeloQr modeloViaje);

    @GET("infomaciones/funListarCuestionario.php")
    Call<JsonElement> getPreguntas();

    @POST("conductores/funCargaCuestionario.php")
    Call<ResponseBody> funCargaCuestionario(@Body Respuesta respuesta);

    @POST("conductores/funChoferesViaje.php")
    Call<Choferesviajes> funChoferesViaje(@Body ModeloViajeId modeloViaje);

    @GET("infomaciones/funListarCuestionarioChofer.php")
    Call<JsonElement> getPreguntasChofer();

    @POST("conductores/funCargaCuestionarioCp.php")
    Call<ResponseBody> funCargaCuestionarioCp(@Body RespuestaChofer respuesta);

    @POST("conductores/funCargaCuestionarioSc.php")
    Call<ResponseBody> funCargaCuestionarioSc(@Body RespuestaChofer respuesta);

    @POST("conductores/funCargaCuestionarioAux.php")
    Call<ResponseBody> funCargaCuestionarioAux(@Body RespuestaChofer respuesta);

}
