package com.pullmandelnorte.spencechofer.modelo;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Empleado {
    @Expose
    private String id;
    @Expose
    private String nombre_completo;
    @Expose
    private String cedula;
}
