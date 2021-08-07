package com.pullmandelnorte.spencechofer.ws.api.apiServices;

import com.pullmandelnorte.spencechofer.entities.Registro;
import com.pullmandelnorte.spencechofer.modelo.Mensaje;
import com.pullmandelnorte.spencechofer.modelo.ModeloUsuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegistroService {
    @POST("conductores/funAgregarUsuario.php")
    Call<Mensaje> enviarUsuario(@Body Registro registro);
    @POST("conductores/funModificarUsuario.php")
    Call<Mensaje> modificarUsuario(@Body ModeloUsuario usuario);
}
