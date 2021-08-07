package com.pullmandelnorte.spencechofer.entities;

import com.google.gson.annotations.Expose;
import com.orm.SugarRecord;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Registro extends SugarRecord implements Serializable {
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
    private String version;
}
