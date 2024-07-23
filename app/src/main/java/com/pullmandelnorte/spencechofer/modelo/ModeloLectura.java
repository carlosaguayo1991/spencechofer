package com.pullmandelnorte.spencechofer.modelo;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModeloLectura {
    @Expose
    private String idviajes;
    @Expose
    private String cod_barra;
}
