package com.pullmandelnorte.spencechofer.modelo;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModeloUsuario2 implements Serializable {
    @Expose
    private String nombreusuario;
    @Expose
    private String telefono;
    @Expose
    private String email;
    @Expose
    private String cedula;
    @Expose
    private String direccion;
    @Expose
    private String version;
}
