package com.pullmandelnorte.spencechofer.modelo;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmpleadoCarga {
    @Expose
    private String cedula;
    @Expose
    private String idviaje;
    @Expose
    private String asiento;
    @Expose
    private String temperatura;
}
