package com.pullmandelnorte.spencechofer.modelo;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModeloUsuario {
    @Expose
    private String nombreusuario;
    @Expose
    private String numerotelefono;
    @Expose
    private String email;
    @Expose
    private String cedula;
    @Expose
    private String direccion;
    @Expose
    private String pass;
}
