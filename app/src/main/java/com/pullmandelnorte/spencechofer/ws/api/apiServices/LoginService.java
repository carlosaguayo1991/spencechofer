package com.pullmandelnorte.spencechofer.ws.api.apiServices;

import com.pullmandelnorte.spencechofer.modelo.ModeloLogin;
import com.pullmandelnorte.spencechofer.modelo.ModeloUsuario2;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

//    @POST("trabajadores/inicio_sesion.php")
    @POST("conductores/funInicioSesion.php")
    Call<ModeloUsuario2> login(@Body ModeloLogin modeloLogin);
//    @GET("infomaciones/funListarmensaje.php")
//    Call<Mensaje> getMensaje();
}
